package br.edu.utfpr.tsi.utfparking.data;

import br.edu.utfpr.tsi.utfparking.models.entities.AccessCard;
import br.edu.utfpr.tsi.utfparking.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAccessCard(AccessCard accessCard);

    Page<User> findAllByNameIsContainingOrAccessCardUsernameIsContaining(String name, String username, Pageable pageable);
}
