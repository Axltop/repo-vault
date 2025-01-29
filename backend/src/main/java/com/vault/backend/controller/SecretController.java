package com.vault.backend.controller;



import com.vault.backend.dto.SecretDTO;
import com.vault.backend.exception.ResourceNotFound;
import com.vault.backend.model.SecretEntity;
import com.vault.backend.service.SecretService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Secret Controller
@RestController
@RequestMapping("/api/secrets")
public class SecretController {

    private final SecretService secretService;

    public SecretController(SecretService secretService) {
        this.secretService = secretService;
    }

    @PostMapping("/{repositoryId}")
    public ResponseEntity<SecretEntity> addSecret(@PathVariable Long repositoryId, @RequestBody SecretDTO secretDTO) throws ResourceNotFound {
        SecretEntity secret = secretService.addSecret(repositoryId, secretDTO.getSecret());
        return ResponseEntity.ok(secret);
    }

    @DeleteMapping("/{repositoryId}/{secretId}")
    public ResponseEntity<Void> deleteSecret(@PathVariable Long repositoryId, @PathVariable Long secretId) throws ResourceNotFound {
        secretService.deleteSecret(repositoryId, secretId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{repositoryId}/validate")
    public ResponseEntity<Boolean> validateSecret(@PathVariable Long repositoryId, @RequestBody SecretDTO secretDTO) {
        boolean isValid = secretService.validateSecret(repositoryId, secretDTO.getSecret());
        return ResponseEntity.ok(isValid);
    }
}