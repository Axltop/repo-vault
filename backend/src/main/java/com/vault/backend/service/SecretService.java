package com.vault.backend.service;

import com.vault.backend.dto.SecretDTO;
import com.vault.backend.exception.ResourceNotFound;
import com.vault.backend.model.SecretEntity;
import com.vault.backend.repository.SecretRepository;
import com.vault.backend.utils.AESGCMEncryptionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecretService {

    @Value("${encrypt-key}")
    private String encryptKey;

    private final SecretRepository secretRepository;

    public SecretService(final SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    public SecretEntity addSecret(SecretDTO secretDTO) throws Exception {
        String hashedSecret = AESGCMEncryptionUtil.encrypt(secretDTO.getSecret(),AESGCMEncryptionUtil.generateKey(encryptKey));
        SecretEntity secret = new SecretEntity(hashedSecret, secretDTO.getRepoId());
        return secretRepository.save(secret);
    }

    public void deleteSecret( Long secretId) throws ResourceNotFound {
        SecretEntity secret = secretRepository.findById(secretId)
                .orElseThrow(ResourceNotFound::new);
        secretRepository.delete(secret);
    }

    public boolean validateSecret(Long id, String rawSecret) throws ResourceNotFound, Exception {
            List<SecretEntity> secrets = secretRepository.findByRepositoryId(id);
            for (SecretEntity secret : secrets){
                if (AESGCMEncryptionUtil.decrypt(secret.getSecret(),AESGCMEncryptionUtil.generateKey(encryptKey)).equals(rawSecret)) {
                    return true;
                }
            }
        return false;
    }


    public SecretDTO decodeSecret(SecretDTO secretDTO) throws ResourceNotFound, Exception {
        SecretEntity secret = secretRepository.findById(secretDTO.getId()).orElseThrow(ResourceNotFound::new);
        String decodedSecret = AESGCMEncryptionUtil.decrypt(secret.getSecret(),AESGCMEncryptionUtil.generateKey(encryptKey));
        SecretDTO result = new SecretDTO();
        result.setId(secret.getId());
        result.setRepoId(secret.getRepositoryId());
        result.setSecret(decodedSecret);

        return result;

    }
}