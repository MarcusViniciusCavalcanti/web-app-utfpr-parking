package br.edu.utfpr.tsi.utfparking.models.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    private Long id;

    private String plate;

    private String model;

}
