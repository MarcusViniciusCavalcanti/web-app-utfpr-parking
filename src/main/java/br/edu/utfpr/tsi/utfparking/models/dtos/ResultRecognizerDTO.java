package br.edu.utfpr.tsi.utfparking.models.dtos;

import br.edu.utfpr.tsi.utfparking.models.entities.Car;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResultRecognizerDTO {
    private Car car;
    private Float confidence;
}
