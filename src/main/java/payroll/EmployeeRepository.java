package payroll;

import org.springframework.data.mongodb.repository.MongoRepository;

interface EmployeeRepository extends MongoRepository<Employee, String> {

}
