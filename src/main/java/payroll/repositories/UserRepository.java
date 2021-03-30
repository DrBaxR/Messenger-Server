package payroll.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import payroll.entities.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    Boolean existsByEmail(String email);
}
