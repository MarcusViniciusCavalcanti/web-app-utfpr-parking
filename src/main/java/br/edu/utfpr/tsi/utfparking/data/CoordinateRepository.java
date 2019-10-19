package br.edu.utfpr.tsi.utfparking.data;

import br.edu.utfpr.tsi.utfparking.models.entities.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
}
