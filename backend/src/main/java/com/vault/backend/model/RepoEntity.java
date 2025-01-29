package com.vault.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "repository")
public class RepoEntity {
//    TODO store url in db

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String url;

    @JsonIgnore
    @OneToMany(mappedBy = "repository", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SecretEntity> secrets;

    public RepoEntity() {
    }

    public RepoEntity(String url) {
        this.url = url;
    }
}