package com.vault.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RepoDTO {

    private String url;

    public RepoDTO(String url) {
        this.url = url;
    }

}
