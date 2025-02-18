package com.vault.backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "repository")
public class RepoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String url;

    @JsonInclude
    @OneToMany(mappedBy = "repositoryId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SecretEntity> secrets;

    public RepoEntity() {
    }

    public RepoEntity(String url) {
        this.url = url;
    }
}