package com.vault.backend.controller;

import com.vault.backend.dto.SecretDTO;
import com.vault.backend.exception.ResourceNotFound;
import com.vault.backend.model.SecretEntity;
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
        SecretDTO secretDTO = new SecretDTO(1L,"12345");
       secretController.addSecret(secretDTO);

       verify(secretService,times(1)).addSecret(secretDTO);
    }

    @Test
    void deleteTest() throws ResourceNotFound {
        secretController.deleteSecret(1L);
        verify(secretService,times(1)).deleteSecret(1L);
    }

    @Test
    void validateTest() throws ResourceNotFound {
        SecretDTO secretDTO = new SecretDTO();
        secretDTO.setSecret("12345");
        secretDTO.setId(1L);

        secretController.validateSecret(secretDTO);
        verify(secretService,times(1)).validateSecret(1L,secretDTO.getSecret());
    }

}
