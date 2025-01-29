package com.vault.backend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vault.backend.dto.RepoDTO;
import com.vault.backend.dto.SecretDTO;
import com.vault.backend.exception.FieldNotUnique;
import com.vault.backend.exception.ResourceNotFound;
import com.vault.backend.model.RepoEntity;
import com.vault.backend.service.RepoService;
import com.vault.backend.service.SecretService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.random.RandomGenerator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecretControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SecretService secretService;

    @Autowired
    private RepoService repoService;

    @Autowired
    private ObjectMapper objectMapper;

    private SecretDTO secretDTO;

    private RepoEntity repoEntity;

    private RandomGenerator randomGenerator = RandomGenerator.getDefault();
    @BeforeEach
    public void setUp() throws FieldNotUnique {
        RepoDTO newRepo = new RepoDTO("http://"+ randomGenerator.ints().toString()+".com");
       repoEntity=  repoService.addRepository(newRepo.getUrl());
    }

    @Test
    public void testAddSecret() throws Exception {

        secretDTO = new SecretDTO(repoEntity.getId(), "12345");

        mockMvc.perform(post("/api/secrets/"+repoEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secretDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testDeleteSecret() throws Exception, ResourceNotFound {

        secretService.addSecret(repoEntity.getId(),"12345");
        mockMvc.perform(delete("/api/secrets/{key}/{key2}", repoEntity.getId(),1))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(delete("/api/secrets/{key}/{key2}", repoEntity.getId(),1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}