package com.vault.backend.controller;

import com.vault.backend.dto.SecretDTO;
import com.vault.backend.exception.ResourceNotFound;
import com.vault.backend.service.SecretService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SecretControllerUnitTest {

    @Mock
    private SecretService secretService;

    @InjectMocks
    private SecretController secretController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTest() throws ResourceNotFound {
       secretController.addSecret(1L,new SecretDTO(1L,"12345"));

       verify(secretService,times(1)).addSecret(1L,"12345");
    }

    @Test
    void deleteTest() throws ResourceNotFound {
        secretController.deleteSecret(1L,1L);
        verify(secretService,times(1)).deleteSecret(1L,1L);
    }

    @Test
    void validateTest(){
        SecretDTO secretDTO = new SecretDTO();
        secretDTO.setSecret("12345");
        secretController.validateSecret(1L,secretDTO);
        verify(secretService,times(1)).validateSecret(1L,secretDTO.getSecret());
    }

}
