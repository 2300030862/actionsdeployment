package com.actions.deployment.service.impl;

import com.actions.deployment.dto.EmployeeDTO;
import com.actions.deployment.entity.Employee;
import com.actions.deployment.repository.EmployeeRepository;
import com.actions.deployment.service.EmployeeService;
import com.actions.deployment.util.EmployeeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Employee Service Implementation
 * Provides business logic for employee operations
 */
@Service
@Transactional
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getAllActiveEmployees() {
        log.info("Fetching all active employees");
        return employeeRepository.findByIsActiveTrue()
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        log.info("Fetching employee with ID: {}", id);
        return employeeRepository.findById(id)
                .map(employeeMapper::toDTO);
    }

    @Override
    public Optional<EmployeeDTO> getEmployeeByEmail(String email) {
        log.info("Fetching employee with email: {}", email);
        return employeeRepository.findByEmail(email)
                .map(employeeMapper::toDTO);
    }

    @Override
    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        log.info("Fetching employees from department: {}", department);
        return employeeRepository.findByDepartment(department)
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByFirstName(String firstName) {
        log.info("Fetching employees with first name: {}", firstName);
        return employeeRepository.findByFirstNameIgnoreCase(firstName)
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByLastName(String lastName) {
        log.info("Fetching employees with last name: {}", lastName);
        return employeeRepository.findByLastNameIgnoreCase(lastName)
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByPosition(String position) {
        log.info("Fetching employees with position: {}", position);
        return employeeRepository.findByPosition(position)
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        log.info("Creating new employee with email: {}", employeeDTO.getEmail());

        // Check if email already exists
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            log.warn("Email already exists: {}", employeeDTO.getEmail());
            throw new IllegalArgumentException("Email already exists: " + employeeDTO.getEmail());
        }

        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setIsActive(true);

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());

        return employeeMapper.toDTO(savedEmployee);
    }

    @Override
    public Optional<EmployeeDTO> updateEmployee(Long id, EmployeeDTO employeeDTO) {
        log.info("Updating employee with ID: {}", id);

        return employeeRepository.findById(id)
                .map(existingEmployee -> {
                    // Update fields
                    if (employeeDTO.getFirstName() != null) {
                        existingEmployee.setFirstName(employeeDTO.getFirstName());
                    }
                    if (employeeDTO.getLastName() != null) {
                        existingEmployee.setLastName(employeeDTO.getLastName());
                    }
                    if (employeeDTO.getEmail() != null && !existingEmployee.getEmail().equals(employeeDTO.getEmail())) {
                        // Check if new email already exists
                        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
                            log.warn("Email already exists: {}", employeeDTO.getEmail());
                            throw new IllegalArgumentException("Email already exists: " + employeeDTO.getEmail());
                        }
                        existingEmployee.setEmail(employeeDTO.getEmail());
                    }
                    if (employeeDTO.getPhone() != null) {
                        existingEmployee.setPhone(employeeDTO.getPhone());
                    }
                    if (employeeDTO.getDepartment() != null) {
                        existingEmployee.setDepartment(employeeDTO.getDepartment());
                    }
                    if (employeeDTO.getPosition() != null) {
                        existingEmployee.setPosition(employeeDTO.getPosition());
                    }
                    if (employeeDTO.getSalary() != null) {
                        existingEmployee.setSalary(employeeDTO.getSalary());
                    }
                    if (employeeDTO.getHireDate() != null) {
                        existingEmployee.setHireDate(employeeDTO.getHireDate());
                    }
                    if (employeeDTO.getIsActive() != null) {
                        existingEmployee.setIsActive(employeeDTO.getIsActive());
                    }

                    existingEmployee.setUpdatedAt(LocalDateTime.now());
                    Employee updatedEmployee = employeeRepository.save(existingEmployee);
                    log.info("Employee updated successfully with ID: {}", id);

                    return employeeMapper.toDTO(updatedEmployee);
                });
    }

    @Override
    public boolean deleteEmployee(Long id) {
        log.info("Deleting employee with ID: {}", id);

        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            log.info("Employee deleted successfully with ID: {}", id);
            return true;
        }

        log.warn("Employee not found for deletion with ID: {}", id);
        return false;
    }

    @Override
    public Optional<EmployeeDTO> softDeleteEmployee(Long id) {
        log.info("Soft deleting employee with ID: {}", id);

        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setIsActive(false);
                    employee.setUpdatedAt(LocalDateTime.now());
                    Employee updatedEmployee = employeeRepository.save(employee);
                    log.info("Employee soft deleted successfully with ID: {}", id);
                    return employeeMapper.toDTO(updatedEmployee);
                });
    }

    @Override
    public long getTotalEmployeeCount() {
        long count = employeeRepository.count();
        log.info("Total employee count: {}", count);
        return count;
    }

    @Override
    public long getActiveEmployeeCount() {
        long count = employeeRepository.findByIsActiveTrue().size();
        log.info("Active employee count: {}", count);
        return count;
    }
}

