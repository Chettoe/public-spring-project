package com.jorken.web;

import com.jorken.conf.JorkenSecurityConfig;
import com.jorken.conf.JorkenUserService;
import com.jorken.domain.Betreuer.Betreuer;
import com.jorken.domain.Betreuer.ThemaView;
import com.jorken.service.betreuer.BetreuerService;
import com.jorken.web.securityMock.MockWithOAuth2User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({JorkenSecurityConfig.class})
@WebMvcTest(BetreuerController.class)
public class BetreuerControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    BetreuerService service;

    @MockitoBean
    JorkenUserService userService;

    static final Betreuer dummy = Betreuer.of(
            "null",
            "null",
            "null",
            "0",
            "null",
            "dummy"
    );

    static final ThemaView themaDummy =  new ThemaView(
            UUID.randomUUID(),
            "tummy",
            "splish",
            Set.of("one", "two"),
            Set.of("http://hhu.de")
    );

    @Test
    @DisplayName("Jemand mit Betreuer rechten hat Zugriff auf die Betreuer Seite")
    @MockWithOAuth2User(roles = "BETREUER")
    void test_1() throws Exception {
        when(service.betreuerByGitHubName("dummy")).thenReturn(dummy);
        mvc.perform(get("/jorken/betreuer/profile")
                .with(oauth2Login()
                        .attributes(e -> e.put("login", "dummy"))
                        .authorities(new SimpleGrantedAuthority("ROLE_BETREUER"))))
                .andExpect(status().isOk())
                .andExpect(view().name("betreuer/profile"))
                .andExpect(model().attributeExists("betreuer"));
    }

    @Test
    @DisplayName("Jemand ohne Betreuer rechte hat keinen Zugriff auf die Betreuer Seite")
    void test_2() throws Exception{
        mvc.perform(get("/jorken/betreuer")
                        .with(oauth2Login()
                                .attributes(e -> e.put("login", "dummy"))
                                .authorities(new SimpleGrantedAuthority("ROLE_NON"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Jemand der nicht authentifiziert ist hat keinen Zugriff auf die Betreuer Seite")
    void test_3() throws Exception{
        mvc.perform(get("/jorken/betreuer"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Es kann die Beschreibung eine Betreuers geändert werden")
    @MockWithOAuth2User(roles = "BETREUER")
    void test_4() throws Exception {
        when(service.betreuerByGitHubName("dummy")).thenReturn(dummy);
        mvc.perform(post("/jorken/betreuer/edit_profile")
                        .with(oauth2Login()
                                .attributes(a -> a.put("login", "dummy"))
                                .authorities(new SimpleGrantedAuthority("ROLE_BETREUER")))
                        .with(csrf())
                .param("email", "email@email")
                .param("phoneNumber", "123")
                .param("beschreibung", "bla")
                .param("tags","#tag1#tag2")
                                .param("links", "link1,link2")
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:profile"));

        verify(service).setEmail(dummy.getId(), "email@email");
        verify(service).setPhoneNumber(dummy.getId(), "123");
        verify(service).addBeschreibung(dummy.getId(), "bla");
        verify(service).setTags(dummy.getId(), Set.of("tag1", "tag2"));
        verify(service).setLinks(dummy.getId(), Set.of("link1", "link2"));
    }

    @Test
    @DisplayName("Es kann ein neues Thema erstellt werden")
    @MockWithOAuth2User(roles = "BETREUER")
    void test_5() throws Exception {
        MockMultipartFile file = new MockMultipartFile("markdown", "thema.md", "text/markdown", "splish".getBytes());

        when(service.betreuerByGitHubName("dummy")).thenReturn(dummy);
        mvc.perform(multipart("/jorken/betreuer/create_thema")
                        .file(file)
                        .with(oauth2Login()
                                .attributes(a -> a.put("login", "dummy"))
                                .authorities(new SimpleGrantedAuthority("ROLE_BETREUER")))
                        .with(csrf())
                        .param("name", "propra2")
                        .param("requirements","#req1#req2")
                        .param("links", "testlink")
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:profile"));

        verify(service).addThema(dummy.getId(), "propra2", "splish", Set.of("req1", "req2"), Set.of("testlink"));
    }

    @Disabled
    @Test
    @DisplayName("Es kann ein vorhandenes Thema bearbeitet werden")
    void test_6() throws Exception {
        when(service.betreuerByGitHubName("dummy")).thenReturn(dummy);
        when(service.themaWithUUID(dummy.getId(), themaDummy.fachId())).thenReturn(themaDummy);
        mvc.perform(post("/jorken/betreuer/edit_thema")
                        .with(oauth2Login()
                                .attributes(a -> a.put("login", "dummy"))
                                .authorities(new SimpleGrantedAuthority("ROLE_BETREUER")))
                        .with(csrf())
                        .param("name", "propra2")
                        .param("markdown", "smth")
                        .param("requirements","#req1#req2")
                        .param("links", "testlink")
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:profile"));

        verify(service).addThema(dummy.getId(), "propra2", "smth", Set.of("req1", "req2"), Set.of("testlink"));
    }

}