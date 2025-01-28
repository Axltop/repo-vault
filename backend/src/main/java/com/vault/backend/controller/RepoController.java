package com.vault.backend.controller;


import com.vault.backend.dto.RepoDTO;
import com.vault.backend.exception.NotFoundException;
import com.vault.backend.model.RepoEntity;
import com.vault.backend.service.RepoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repositories")
public class RepoController {

    private final RepoService repoService;

    public RepoController(RepoService repoService) {
        this.repoService = repoService;
    }

    @PostMapping
    public ResponseEntity<RepoEntity> addRepository(@RequestBody RepoDTO repositoryDTO) {
        RepoEntity repository = repoService.addRepository(repositoryDTO.getUrl());
        return ResponseEntity.ok(repository);
    }

    @GetMapping
    public ResponseEntity<List<RepoEntity>> listRepositories() {
        return ResponseEntity.ok(repoService.listRepositories());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepository(@PathVariable Long id) throws NotFoundException {
        repoService.deleteRepository(id);
        return ResponseEntity.noContent().build();
    }
}
