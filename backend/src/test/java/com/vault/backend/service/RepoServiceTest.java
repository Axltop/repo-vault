package com.vault.backend.service;

import com.vault.backend.exception.FieldNotUnique;
import com.vault.backend.exception.ResourceNotFound;
import com.vault.backend.model.RepoEntity;
import com.vault.backend.repository.RepoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RepoServiceTest {

    @Mock
    private RepoRepository repositoryRepository;

    @InjectMocks
    private RepoService repoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRepository() throws FieldNotUnique {
        String url = "http://example.com";
        RepoEntity repository = new RepoEntity(url);
        when(repositoryRepository.save(any(RepoEntity.class))).thenReturn(repository);

        RepoEntity result = repoService.addRepository(url);

        assertThat(result).isNotNull();
        assertThat(result.getUrl()).isEqualTo(url);
        verify(repositoryRepository, times(1)).save(any(RepoEntity.class));
    }

    @Test
    void testListRepositories() {
        RepoEntity repo1 = new RepoEntity("http://repo1.com");
        RepoEntity repo2 = new RepoEntity("http://repo2.com");
        when(repositoryRepository.findAll()).thenReturn(Arrays.asList(repo1, repo2));

        List<RepoEntity> result = repoService.listRepositories();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).containsExactly(repo1, repo2);
        verify(repositoryRepository, times(1)).findAll();
    }

    @Test
    void testDeleteRepository() throws ResourceNotFound {
        Long id = 1L;
        when(repositoryRepository.findOneById(id)).thenReturn(Optional.of(new RepoEntity("http://repo1.com")));

        repoService.deleteRepository(id);

        verify(repositoryRepository, times(1)).deleteById(id);
    }

    @Test
    void addRepositoryNotUnique() {
        String url = "http://example.com";
        RepoEntity repository = new RepoEntity(url);
        when(repositoryRepository.existsByUrl(url)).thenReturn(true);
        FieldNotUnique exception = assertThrows(FieldNotUnique.class, () -> {
            repoService.addRepository(url);
        });
        verify(repositoryRepository, never()).save(any(RepoEntity.class));
        verify(repositoryRepository, times(1)).existsByUrl(url);
        assertEquals(String.format("%s is already saved.", url), exception.getMessage());
    }

    @Test
    void getRepository() throws ResourceNotFound {

        when(repositoryRepository.findOneById(1L)).thenReturn(Optional.of(new RepoEntity("http://example.com")));
        repoService.getRepository(1L);
        verify(repositoryRepository, times(1)).findOneById(1L);
    }

    @Test
    void getRepositoryNoResource() throws ResourceNotFound {
        when(repositoryRepository.findOneById(1L)).thenReturn(Optional.empty());

        ResourceNotFound exception = assertThrows(ResourceNotFound.class, () -> {
            repoService.getRepository(1L);
        });
        verify(repositoryRepository, times(1)).findOneById(1L);
    }
}