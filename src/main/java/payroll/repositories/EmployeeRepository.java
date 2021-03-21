package payroll.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import payroll.entities.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

}
