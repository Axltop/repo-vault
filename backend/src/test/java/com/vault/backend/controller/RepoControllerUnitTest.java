package com.vault.backend.controller;

import com.vault.backend.dto.RepoDTO;
import com.vault.backend.exception.NotFoundException;
import com.vault.backend.repository.RepoRepository;
import com.vault.backend.service.RepoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class RepoControllerUnitTest {
    @Mock
    private RepoRepository repoRepository;
    @Mock
    private RepoService repoService;

    @InjectMocks
    private RepoController repoController;


    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void listTest() {
        repoController.listRepositories();
        verify(repoService).listRepositories();
    }

    @Test
    void addTest() {
        String repoURL = "http://example.com";
        RepoDTO repoDTO = new RepoDTO(repoURL);

        repoController.addRepository(repoDTO);
        verify(repoService).addRepository(repoURL);
    }

    @Test
    void deleteTest() throws NotFoundException {
        repoController.deleteRepository(1l);
        verify(repoService).deleteRepository(1l);
    }
}
