package com.jorken.web;

import com.jorken.service.betreuer.BetreuerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    BetreuerService service;

    @Test
    @DisplayName("Die startseite ist erreichbar")
    void test_1() throws Exception{
        mvc.perform(get("/jorken"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Die suche nach Betreuern gibt das Betreuer Ergebniss zurück")
    void test_2() throws Exception {
        mvc.perform(post("/jorken")
                .param("search", "suche")
                .param("filter", "betreuer")
                .with(csrf())
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Die suche nach Themen gibt das Themen Ergebniss zurück")
    void test_3() throws Exception {
        mvc.perform(post("/jorken")
                .param("search", "suche")
                .param("filter", "themen")
                .with(csrf())
        ).andExpect(status().is3xxRedirection());
    }
}