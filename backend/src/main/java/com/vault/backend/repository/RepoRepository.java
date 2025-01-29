package com.vault.backend.repository;

import com.vault.backend.model.RepoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RepoRepository extends JpaRepository<RepoEntity, Long> {

    Optional<RepoEntity> findOneById(Long id);

    boolean existsByUrl(String url);
}
