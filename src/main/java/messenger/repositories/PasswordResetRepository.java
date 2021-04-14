package messenger.repositories;

import messenger.entities.PasswordReset;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordResetRepository extends MongoRepository<PasswordReset, String> {
    Optional<PasswordReset> findByEmail(String email);
}
