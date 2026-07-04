package com.jorken.web;

import com.jorken.conf.JorkenMethodSecurityConfig;
import com.jorken.conf.JorkenSecurityConfig;
import com.jorken.conf.JorkenUserService;
import com.jorken.service.betreuer.BetreuerService;
import com.jorken.web.securityMock.MockWithOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({JorkenSecurityConfig.class})
@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    BetreuerService service;

    @MockitoBean
    JorkenUserService userService;

    @Test
    @DisplayName("Es kann ein Betreuer hinzugefügt werden")
    @MockWithOAuth2User(roles = "ADMIN")
    void test_1() throws Exception{
        mvc.perform(post("/jorken/admin/add")
                        .with(csrf())
                        .param("firstName", "Jens")
                        .param("lastName", "Bendisposto")
                        .param("email", "Email@email")
                        .param("phoneNumber", "123")
                        .param("gitHubName", "git")
        ).andExpect(status().is3xxRedirection());

        verify(service).addBetreuer("Jens",
                "Bendisposto",
                "Email@email",
                "123",
                "git");
    }

    @Test
    @DisplayName("Die admin Seite ist für eine Person mit der Rolle ADMIN erreichbar")
    @MockWithOAuth2User(roles = "ADMIN")
    void test_2() throws Exception{
        mvc.perform(get("/jorken/admin").with(csrf()))
                .andExpect(status().isOk());

        mvc.perform(get("/jorken/admin/add").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User die nicht authentifiziert wurden können nicht auf die Admin Seite zugreifen")
    void test_3() throws Exception{
        mvc.perform(get("/jorken/admin"))
                .andExpect(status().is3xxRedirection());

        mvc.perform(get("/jorken/admin/add"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("User die keine Admin rolle haben können nicht auf die Admin Seite zugreifen")
    @MockWithOAuth2User
    void set_4() throws Exception{
        mvc.perform(get("/jorken/admin"))
                .andExpect(status().isForbidden());

        mvc.perform(get("/jorken/admin/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Werden keine Parameter bei dem hinzufügen eines Betreuers übergeben wird das form neu geladen")
    @MockWithOAuth2User(roles = "ADMIN")
    void test_5() throws Exception {
        mvc.perform(post("/jorken/admin/add").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/addBetreuerForm"));
    }
}