package com.vault.backend.repository;

import com.vault.backend.model.SecretEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SecretRepository extends JpaRepository<SecretEntity, Long> {

    Optional<SecretEntity> findByIdAndRepositoryId(Long id, Long repositoryId);
    
    List<SecretEntity> findByRepositoryId(Long repositoryId);

}