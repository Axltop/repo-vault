package com.vault.backend.repository;

import com.vault.backend.model.SecretEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecretRepository extends JpaRepository<SecretEntity, Long> {
    List<SecretEntity> findByRepositoryId(Long repositoryId);

}