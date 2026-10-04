package com.altees.OpenAIExample.repostiory;

import com.altees.OpenAIExample.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Employee entity
 * Provides CRUD operations and custom queries
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByName(String name);

}

