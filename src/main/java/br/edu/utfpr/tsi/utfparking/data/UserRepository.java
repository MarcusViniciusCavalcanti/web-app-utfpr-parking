package br.edu.utfpr.tsi.utfparking.data;

import br.edu.utfpr.tsi.utfparking.models.entities.AccessCard;
import br.edu.utfpr.tsi.utfparking.models.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByAccessCard(AccessCard accessCard);
}
