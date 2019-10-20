package br.edu.utfpr.tsi.utfparking.models.dtos;

import lombok.Data;

@Data
public class ApplicationConfigDTO {
    private Long id;

    private String modeSystem;

    private String ip;
}
