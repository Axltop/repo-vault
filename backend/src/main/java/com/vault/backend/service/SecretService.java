package com.vault.backend.service;

import com.vault.backend.dto.SecretDTO;
import com.vault.backend.exception.ResourceNotFound;
import com.vault.backend.repository.RepoRepository;
import com.vault.backend.model.RepoEntity;
import com.vault.backend.model.SecretEntity;
import com.vault.backend.repository.SecretRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecretService {

    private final SecretRepository secretRepository;
    private final PasswordEncoder passwordEncoder;

    public SecretService(final SecretRepository secretRepository, PasswordEncoder passwordEncoder) {
        this.secretRepository = secretRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SecretEntity addSecret(SecretDTO secretDTO) throws ResourceNotFound {
        String hashedSecret = passwordEncoder.encode(secretDTO.getSecret());
        SecretEntity secret = new SecretEntity(hashedSecret, secretDTO.getRepoId());
        return secretRepository.save(secret);
    }

    public void deleteSecret( Long secretId) throws ResourceNotFound {
        SecretEntity secret = secretRepository.findById(secretId)
                .orElseThrow(ResourceNotFound::new);
        secretRepository.delete(secret);
    }

    public boolean validateSecret(Long id, String rawSecret) throws ResourceNotFound {
            SecretEntity secret = secretRepository.findById(id).orElseThrow(ResourceNotFound::new);
            if (passwordEncoder.matches(rawSecret, secret.getSecret())) {
                return true;
            }
        return false;
    }


}