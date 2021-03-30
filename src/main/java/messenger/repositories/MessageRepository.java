package messenger.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import messenger.entities.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
}
