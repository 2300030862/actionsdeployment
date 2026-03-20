package com.actions.deployment.service;

import com.actions.deployment.dto.EmployeeDTO;
import java.util.List;
import java.util.Optional;

/**
 * Employee Service Interface
 * Defines business logic operations for employees
 */
public interface EmployeeService {

    /**
     * Get all employees
     * @return list of all employees
     */
    List<EmployeeDTO> getAllEmployees();

    /**
     * Get all active employees
     * @return list of active employees
     */
    List<EmployeeDTO> getAllActiveEmployees();

    /**
     * Get employee by ID
     * @param id the employee ID
     * @return Optional containing the employee if found
     */
    Optional<EmployeeDTO> getEmployeeById(Long id);

    /**
     * Get employee by email
     * @param email the employee's email
     * @return Optional containing the employee if found
     */
    Optional<EmployeeDTO> getEmployeeByEmail(String email);

    /**
     * Get employees by department
     * @param department the department name
     * @return list of employees in the department
     */
    List<EmployeeDTO> getEmployeesByDepartment(String department);

    /**
     * Get employees by first name
     * @param firstName the first name
     * @return list of employees with matching first name
     */
    List<EmployeeDTO> getEmployeesByFirstName(String firstName);

    /**
     * Get employees by last name
     * @param lastName the last name
     * @return list of employees with matching last name
     */
    List<EmployeeDTO> getEmployeesByLastName(String lastName);

    /**
     * Get employees by position
     * @param position the position
     * @return list of employees with the position
     */
    List<EmployeeDTO> getEmployeesByPosition(String position);

    /**
     * Create a new employee
     * @param employeeDTO the employee data
     * @return the created employee
     */
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    /**
     * Update an existing employee
     * @param id the employee ID
     * @param employeeDTO the updated employee data
     * @return Optional containing the updated employee if found
     */
    Optional<EmployeeDTO> updateEmployee(Long id, EmployeeDTO employeeDTO);

    /**
     * Delete an employee by ID
     * @param id the employee ID
     * @return true if deleted, false if not found
     */
    boolean deleteEmployee(Long id);

    /**
     * Soft delete an employee (mark as inactive)
     * @param id the employee ID
     * @return Optional containing the updated employee if found
     */
    Optional<EmployeeDTO> softDeleteEmployee(Long id);

    /**
     * Count total employees
     * @return total count of employees
     */
    long getTotalEmployeeCount();

    /**
     * Count active employees
     * @return count of active employees
     */
    long getActiveEmployeeCount();
}

