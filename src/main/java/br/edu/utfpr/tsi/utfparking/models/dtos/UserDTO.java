package br.edu.utfpr.tsi.utfparking.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;

    private String name;

    private AccessCardDTO accessCard;
}
