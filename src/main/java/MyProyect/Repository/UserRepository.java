package MyProyect.Repository;

import MyProyect.Model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(@NotBlank @Size(min = 8, max = 15) String username);

    boolean existsByUsername(@NotBlank @Size(min = 8, max = 15) String username);
}
