package br.edu.utfpr.tsi.utfparking.data;

import br.edu.utfpr.tsi.utfparking.models.entities.Car;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends PagingAndSortingRepository<Car, Long> {

    List<Car> findAllByPlateIn(List<String> plates);

}
