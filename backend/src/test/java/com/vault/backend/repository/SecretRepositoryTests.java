package com.vault.backend.repository;

import com.vault.backend.model.RepoEntity;
import com.vault.backend.model.SecretEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SecretRepositoryTests {

    private static final String HASHED_SECRET = "hashed-secret";

    @Autowired
    private SecretRepository secretRepository;

    @Autowired
    private RepoRepository repositoryRepository;

    private RepoEntity repository;
    private SecretEntity secret;

    @BeforeEach
    void setUp() {
        repository = new RepoEntity("http://example.com");
        repository = repositoryRepository.save(repository);
        secret = new SecretEntity(HASHED_SECRET, repository);
    }

    @Test
    public void testSaveSecret() {
        SecretEntity savedSecret = secretRepository.save(secret);

        assertThat(savedSecret).isNotNull();
        assertThat(savedSecret.getId()).isNotNull();
        assertThat(savedSecret.getSecret()).isEqualTo(HASHED_SECRET);
        assertThat(savedSecret.getRepository().getId()).isEqualTo(repository.getId());
    }

    @Test
    public void testFindByIdAndRepositoryId() {
        secret = secretRepository.save(secret);

        Optional<SecretEntity> foundSecret = secretRepository.findByIdAndRepositoryId(secret.getId(), repository.getId());

        assertThat(foundSecret).isPresent();
        assertThat(foundSecret.get().getSecret()).isEqualTo(HASHED_SECRET);
    }

    @Test
    public void testFindByRepositoryId() {
        SecretEntity secret2 = new SecretEntity("hashed-secret-2", repository);
        secretRepository.save(secret);
        secretRepository.save(secret2);

        List<SecretEntity> secrets = secretRepository.findByRepositoryId(repository.getId());

        assertThat(secrets).hasSize(2);
        assertThat(secrets.get(0).getSecret()).isEqualTo(HASHED_SECRET);
        assertThat(secrets.get(1).getSecret()).isEqualTo("hashed-secret-2");
    }

    @Test
    public void testDeleteSecret() {
        secret = secretRepository.save(secret);

        secretRepository.delete(secret);

        Optional<SecretEntity> foundSecret = secretRepository.findById(secret.getId());
        assertThat(foundSecret).isNotPresent();
    }
}