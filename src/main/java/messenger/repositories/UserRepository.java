package messenger.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import messenger.entities.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    Boolean existsByEmail(String email);
}
