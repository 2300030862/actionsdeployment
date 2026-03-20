package com.actions.deployment.repository;

import com.actions.deployment.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Employee Repository Interface
 * Handles database operations for Employee entity
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Find employee by email
     * @param email the employee's email
     * @return Optional containing the employee if found
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Find all active employees
     * @return list of active employees
     */
    List<Employee> findByIsActiveTrue();

    /**
     * Find all employees by department
     * @param department the department name
     * @return list of employees in the department
     */
    List<Employee> findByDepartment(String department);

    /**
     * Find employees by first name (case-insensitive)
     * @param firstName the first name
     * @return list of employees with matching first name
     */
    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    List<Employee> findByFirstNameIgnoreCase(@Param("firstName") String firstName);

    /**
     * Find employees by last name (case-insensitive)
     * @param lastName the last name
     * @return list of employees with matching last name
     */
    @Query("SELECT e FROM Employee e WHERE LOWER(e.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<Employee> findByLastNameIgnoreCase(@Param("lastName") String lastName);

    /**
     * Check if employee exists by email
     * @param email the email to check
     * @return true if employee exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find employees by position
     * @param position the position
     * @return list of employees with the position
     */
    List<Employee> findByPosition(String position);
}

