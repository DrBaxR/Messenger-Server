package messenger.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import messenger.entities.Group;

public interface GroupRepository extends MongoRepository<Group, String> {
}
