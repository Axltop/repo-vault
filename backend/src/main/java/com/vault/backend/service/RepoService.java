package com.vault.backend.service;


import com.vault.backend.exception.NotFoundException;
import com.vault.backend.model.RepoEntity;
import com.vault.backend.repository.RepoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepoService {

    private final RepoRepository repoRepository;

    public RepoService(RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    public RepoEntity addRepository(String url) {
        RepoEntity repository = new RepoEntity(url);
        return repoRepository.save(repository);
    }

    public List<RepoEntity> listRepositories() {
        return repoRepository.findAll();
    }

    public void deleteRepository(Long id) throws NotFoundException {
        repoRepository.findOneById(id).orElseThrow(NotFoundException::new);
        repoRepository.deleteById(id);
    }
}