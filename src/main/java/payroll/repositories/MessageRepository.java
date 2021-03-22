package payroll.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import payroll.entities.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
}
