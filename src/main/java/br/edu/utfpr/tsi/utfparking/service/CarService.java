package br.edu.utfpr.tsi.utfparking.service;

import br.edu.utfpr.tsi.utfparking.data.CarRepository;
import br.edu.utfpr.tsi.utfparking.models.entities.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CarService {

    private final CarRepository carRepository;

    public List<Car> getCarByPlates(List<String> plates) {
        return carRepository.findAllByPlateIn(plates);
    }
}
