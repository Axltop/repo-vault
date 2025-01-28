package com.vault.backend.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="secret")
public class SecretEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String secret;

    @ManyToOne
    @JoinColumn(name = "repository_id", nullable = false)
    private RepoEntity repository;

    public SecretEntity() {
    }

    public SecretEntity(String secret, RepoEntity repository) {
        this.secret = secret;
        this.repository = repository;
    }

}