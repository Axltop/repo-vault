package com.vault.backend.service;


import com.vault.backend.exception.ResourceNotFound;
import com.vault.backend.model.RepoEntity;
import com.vault.backend.model.SecretEntity;
import com.vault.backend.repository.RepoRepository;
import com.vault.backend.repository.SecretRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SecretServiceTest {

    private static final String HASHED_SECRET = "hashed-secret";
    private static final String RAW_SECRET = "raw-secret";
    private static final Long REPOSITORY_ID = 1L;

    @Mock
    private SecretRepository secretRepository;

    @Mock
    private RepoRepository repoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SecretService secretService;

    private RepoEntity repository;
    private SecretEntity secret;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new RepoEntity("http://example.com");
        secret = new SecretEntity(HASHED_SECRET, repository);
    }

    @Test
    public void testAddSecret() throws ResourceNotFound {
        repository.setId(REPOSITORY_ID);

        when(repoRepository.findById(REPOSITORY_ID)).thenReturn(Optional.of(repository));
        when(passwordEncoder.encode(RAW_SECRET)).thenReturn(HASHED_SECRET);
        when(secretRepository.save(any(SecretEntity.class))).thenReturn(new SecretEntity(HASHED_SECRET, repository));

        SecretEntity result = secretService.addSecret(REPOSITORY_ID, RAW_SECRET);

        assertEquals(HASHED_SECRET, result.getSecret());
        assertEquals(REPOSITORY_ID, result.getRepository().getId());
        verify(secretRepository, times(1)).save(any(SecretEntity.class));
    }

    @Test
    public void testDeleteSecret() throws ResourceNotFound {
        Long secretId = 1L;
        secret.setId(secretId);

        when(secretRepository.findByIdAndRepositoryId(secretId, REPOSITORY_ID)).thenReturn(Optional.of(secret));

        secretService.deleteSecret(REPOSITORY_ID, secretId);

        verify(secretRepository, times(1)).delete(secret);
    }

    @Test
    public void testDeleteSecret_NotFound() {
        Long secretId = 999L;

        when(secretRepository.findByIdAndRepositoryId(secretId, REPOSITORY_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> {
            secretService.deleteSecret(REPOSITORY_ID, secretId);
        });
    }

    @Test
    public void testValidateSecret_Valid() {
        when(secretRepository.findByRepositoryId(REPOSITORY_ID)).thenReturn(List.of(secret));
        when(passwordEncoder.matches(RAW_SECRET, HASHED_SECRET)).thenReturn(true);

        boolean result = secretService.validateSecret(REPOSITORY_ID, RAW_SECRET);

        assertTrue(result);
    }

    @Test
    public void testValidateSecret_Invalid() {
        when(secretRepository.findByRepositoryId(REPOSITORY_ID)).thenReturn(Arrays.asList(secret));
        when(passwordEncoder.matches(RAW_SECRET, HASHED_SECRET)).thenReturn(false);

        boolean result = secretService.validateSecret(REPOSITORY_ID, RAW_SECRET);

        assertFalse(result);
    }
}

