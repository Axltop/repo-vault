package com.vault.backend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vault.backend.dto.RepoDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepoControllerTest {

    private static final String REPOSITORY_URL = "http://example.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    public void testAddRepository() throws Exception {
        RepoDTO repositoryDTO = new RepoDTO(REPOSITORY_URL);

        mockMvc.perform(post("/api/repositories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repositoryDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value(REPOSITORY_URL))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @Order(2)
    public void testListRepositories() throws Exception {
        mockMvc.perform(get("/api/repositories"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].url").value(REPOSITORY_URL));
    }

    @Test
    @Order(3)
    public void testDeleteRepository() throws Exception {
        mockMvc.perform(delete("/api/repositories/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testAddRepository_BadRequest() throws Exception {
        mockMvc.perform(post("/api/repositories").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testDeleteRepository_NotFound() throws Exception {
        mockMvc.perform(delete("/api/repositories/{id}", 999L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
