package com.brainstation23.erp.persistence.repository;

import com.brainstation23.erp.persistence.entity.Employee;
import com.brainstation23.erp.persistence.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String email);
}
