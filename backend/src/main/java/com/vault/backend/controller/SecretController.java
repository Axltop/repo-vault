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

    @PostMapping
    public ResponseEntity<SecretEntity> addSecret(@RequestBody SecretDTO secretDTO) throws ResourceNotFound, Exception {
        SecretEntity secret = secretService.addSecret(secretDTO);
        return ResponseEntity.ok(secret);
    }

    @DeleteMapping("/{secretId}")
    public ResponseEntity<Void> deleteSecret(@PathVariable Long secretId) throws ResourceNotFound {
        secretService.deleteSecret(secretId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateSecret( @RequestBody SecretDTO secretDTO) throws ResourceNotFound, Exception {
        boolean isValid = secretService.validateSecret(secretDTO.getRepoId(), secretDTO.getSecret());
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/decode")
    public ResponseEntity<SecretDTO> decodeSecret( @RequestBody SecretDTO secretDTO) throws ResourceNotFound, Exception {
        return ResponseEntity.ok(secretService.decodeSecret(secretDTO));
    }
}