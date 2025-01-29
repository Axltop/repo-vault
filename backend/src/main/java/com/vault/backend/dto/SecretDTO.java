package com.vault.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SecretDTO {
    private Long repoId;
    private Long id;
    private String secret;

    public SecretDTO(Long id, String secret) {
        this.id = id;
        this.secret = secret;
    }
}
