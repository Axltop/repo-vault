package com.vault.backend.service;

import com.vault.backend.dto.SecretDTO;
import com.vault.backend.exception.ResourceNotFound;
import com.vault.backend.model.RepoEntity;
import com.vault.backend.model.SecretEntity;
import com.vault.backend.repository.SecretRepository;
import com.vault.backend.utils.AESGCMEncryptionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SecretServiceTest {

    private static final String HASHED_SECRET = "hashed-secret";
    private static final String RAW_SECRET = "raw-secret";
    private static final Long REPOSITORY_ID = 1L;
    private static final String ENCRYPT_KEY = "super-secret-encrypt-key";

    @Mock
    private SecretRepository secretRepository;

    @Mock
    private RepoService repoService;

    @InjectMocks
    private SecretService secretService;

    private RepoEntity repository;
    private SecretEntity secret;

    @BeforeEach
    void setUp() {
       try( AutoCloseable closeable = MockitoAnnotations.openMocks(this)){
           ReflectionTestUtils.setField(secretService, "encryptKey", ENCRYPT_KEY);

           repository = new RepoEntity("http://example.com");
           secret = new SecretEntity(RAW_SECRET, repository.getId());
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    @Test
    public void testAddSecret() throws ResourceNotFound, Exception {
        repository.setId(REPOSITORY_ID);

        when(repoService.getRepository(REPOSITORY_ID)).thenReturn(repository);
        when(secretRepository.save(any(SecretEntity.class))).thenReturn(new SecretEntity(HASHED_SECRET, repository.getId()));

        SecretEntity result = secretService.addSecret(new SecretDTO(REPOSITORY_ID, null, RAW_SECRET));

        assertEquals(HASHED_SECRET, result.getSecret());
        assertEquals(REPOSITORY_ID, result.getRepositoryId());
        verify(secretRepository, times(1)).save(any(SecretEntity.class));
    }

    @Test
    public void testDeleteSecret() throws ResourceNotFound {
        Long secretId = 1L;
        secret.setId(secretId);

        when(secretRepository.findById(secretId)).thenReturn(Optional.of(secret));

        secretService.deleteSecret(secretId);

        verify(secretRepository, times(1)).delete(secret);
    }

    @Test
    public void testDeleteSecret_NotFound() {
        Long secretId = 999L;

        when(secretRepository.findById(secretId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> secretService.deleteSecret(secretId));
    }

    @Test
    public void testValidateSecret_Valid() throws ResourceNotFound, Exception {
        secret.setSecret(AESGCMEncryptionUtil.encrypt(RAW_SECRET, AESGCMEncryptionUtil.generateKey(ENCRYPT_KEY)));
        secret.setRepositoryId(REPOSITORY_ID);
        when(secretRepository.findByRepositoryId(secret.getId())).thenReturn(List.of(secret));

        boolean result = secretService.validateSecret(secret.getId(), RAW_SECRET);

        assertTrue(result);
    }

    @Test
    public void testValidateSecret_Invalid() throws ResourceNotFound, Exception {
        secret.setSecret(HASHED_SECRET);
        secret.setRepositoryId(REPOSITORY_ID);
        when(secretRepository.findById(secret.getId())).thenReturn(Optional.ofNullable(secret));


        boolean result = secretService.validateSecret(secret.getId(), RAW_SECRET);

        assertFalse(result);
    }
}

