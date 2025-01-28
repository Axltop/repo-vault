package com.vault.backend.service;

import com.vault.backend.exception.NotFoundException;
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
    private final RepoRepository repoRepository;
    private final PasswordEncoder passwordEncoder;

    public SecretService(final SecretRepository secretRepository, RepoRepository repoRepository, final PasswordEncoder passwordEncoder) {
        this.secretRepository = secretRepository;
        this.repoRepository = repoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SecretEntity addSecret(Long repositoryId, String rawSecret) throws NotFoundException {
        RepoEntity repository = repoRepository.findById(repositoryId).orElseThrow(NotFoundException::new);
        String hashedSecret = passwordEncoder.encode(rawSecret);
        SecretEntity secret = new SecretEntity(hashedSecret, repository);
        return secretRepository.save(secret);
    }

    public void deleteSecret(Long repositoryId, Long secretId) throws NotFoundException {
        SecretEntity secret = secretRepository.findByIdAndRepositoryId(secretId, repositoryId)
                .orElseThrow(NotFoundException::new);
        secretRepository.delete(secret);
    }

    public boolean validateSecret(Long repositoryId, String rawSecret) {
        List<SecretEntity> secrets = secretRepository.findByRepositoryId(repositoryId);
        for (SecretEntity secret : secrets) {
            if (passwordEncoder.matches(rawSecret, secret.getSecret())) {
                return true;
            }
        }
        return false;
    }
}