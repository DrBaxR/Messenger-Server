package payroll.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import payroll.entities.Role;
import payroll.entities.RoleName;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(RoleName name);
}
