package payroll.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import payroll.entities.Group;

public interface GroupRepository extends MongoRepository<Group, String> {
}
