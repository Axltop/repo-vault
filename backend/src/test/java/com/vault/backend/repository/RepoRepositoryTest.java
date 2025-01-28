package com.vault.backend.repository;

import com.vault.backend.model.RepoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RepoRepositoryTest {

    private static final String REPOSITORY_URL = "http://example.com";

    @Autowired
    private RepoRepository repositoryRepository;

    private RepoEntity repository;

    @BeforeEach
    void setUp() {
        repository = new RepoEntity(REPOSITORY_URL);
    }

    @Test
    public void testSaveRepository() {
        RepoEntity savedRepository = repositoryRepository.save(repository);

        assertThat(savedRepository).isNotNull();
        assertThat(savedRepository.getId()).isNotNull();
        assertThat(savedRepository.getUrl()).isEqualTo(REPOSITORY_URL);
    }

    @Test
    public void testFindById() {
        RepoEntity savedRepository = repositoryRepository.save(repository);

        Optional<RepoEntity> foundRepository = repositoryRepository.findById(savedRepository.getId());

        assertThat(foundRepository).isPresent();
        assertThat(foundRepository.get().getUrl()).isEqualTo(REPOSITORY_URL);
    }

    @Test
    public void testFindAll() {
        repositoryRepository.save(new RepoEntity("http://repo1.com"));
        repositoryRepository.save(new RepoEntity("http://repo2.com"));

        List<RepoEntity> repositories = repositoryRepository.findAll();

        assertThat(repositories).hasSize(2);
    }

    @Test
    public void testDeleteById() {
        RepoEntity savedRepository = repositoryRepository.save(repository);

        repositoryRepository.deleteById(savedRepository.getId());

        Optional<RepoEntity> foundRepository = repositoryRepository.findById(savedRepository.getId());
        assertThat(foundRepository).isNotPresent();
    }
}
