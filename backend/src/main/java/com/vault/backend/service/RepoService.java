package com.vault.backend.service;


import com.vault.backend.exception.FieldNotUnique;
import com.vault.backend.exception.ResourceNotFound;
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

    public RepoEntity addRepository(String url) throws FieldNotUnique {
        if(repoRepository.existsByUrl(url)){
            throw new FieldNotUnique(url);
        }
        RepoEntity repository = new RepoEntity(url);
        return repoRepository.save(repository);
    }

    public List<RepoEntity> listRepositories() {
        return repoRepository.findAll();
    }

    public void deleteRepository(Long id) throws ResourceNotFound {
        repoRepository.findOneById(id).orElseThrow(ResourceNotFound::new);
        repoRepository.deleteById(id);
    }
}