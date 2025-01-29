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

    @Column(name = "repository_id", nullable = false)
    private Long repositoryId;

    public SecretEntity() {
    }

    public SecretEntity(String secret, Long repositoryId) {
        this.secret = secret;
        this.repositoryId = repositoryId;
    }

}