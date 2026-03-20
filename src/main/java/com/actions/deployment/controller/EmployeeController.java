package com.actions.deployment.controller;

import com.actions.deployment.dto.ApiResponse;
import com.actions.deployment.dto.EmployeeDTO;
import com.actions.deployment.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Employee API endpoints
 */
@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"}, allowCredentials = "true")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Get all employees
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAllEmployees() {
        log.info("GET /employees - Fetching all employees");
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        ApiResponse<List<EmployeeDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Employees retrieved successfully",
                employees
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all active employees
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAllActiveEmployees() {
        log.info("GET /employees/active - Fetching all active employees");
        List<EmployeeDTO> employees = employeeService.getAllActiveEmployees();
        ApiResponse<List<EmployeeDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Active employees retrieved successfully",
                employees
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get employee by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable Long id) {
        log.info("GET /employees/{} - Fetching employee by ID", id);
        Optional<EmployeeDTO> employee = employeeService.getEmployeeById(id);

        if (employee.isPresent()) {
            ApiResponse<EmployeeDTO> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Employee retrieved successfully",
                    employee.get()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ApiResponse<EmployeeDTO> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                "Employee not found",
                "No employee found with ID: " + id
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Get employee by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeByEmail(@PathVariable String email) {
        log.info("GET /employees/email/{} - Fetching employee by email", email);
        Optional<EmployeeDTO> employee = employeeService.getEmployeeByEmail(email);

        if (employee.isPresent()) {
            ApiResponse<EmployeeDTO> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Employee retrieved successfully",
                    employee.get()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ApiResponse<EmployeeDTO> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                "Employee not found",
                "No employee found with email: " + email
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Get employees by department
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployeesByDepartment(@PathVariable String department) {
        log.info("GET /employees/department/{} - Fetching employees by department", department);
        List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment(department);
        ApiResponse<List<EmployeeDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Employees retrieved successfully",
                employees
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get employees by first name
     */
    @GetMapping("/firstname/{firstName}")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployeesByFirstName(@PathVariable String firstName) {
        log.info("GET /employees/firstname/{} - Fetching employees by first name", firstName);
        List<EmployeeDTO> employees = employeeService.getEmployeesByFirstName(firstName);
        ApiResponse<List<EmployeeDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Employees retrieved successfully",
                employees
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get employees by last name
     */
    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployeesByLastName(@PathVariable String lastName) {
        log.info("GET /employees/lastname/{} - Fetching employees by last name", lastName);
        List<EmployeeDTO> employees = employeeService.getEmployeesByLastName(lastName);
        ApiResponse<List<EmployeeDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Employees retrieved successfully",
                employees
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get employees by position
     */
    @GetMapping("/position/{position}")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployeesByPosition(@PathVariable String position) {
        log.info("GET /employees/position/{} - Fetching employees by position", position);
        List<EmployeeDTO> employees = employeeService.getEmployeesByPosition(position);
        ApiResponse<List<EmployeeDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Employees retrieved successfully",
                employees
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create new employee
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("POST /employees - Creating new employee with email: {}", employeeDTO.getEmail());

        try {
            EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
            ApiResponse<EmployeeDTO> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Employee created successfully",
                    createdEmployee
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error creating employee: {}", e.getMessage());
            ApiResponse<EmployeeDTO> response = new ApiResponse<>(
                    HttpStatus.CONFLICT.value(),
                    "Conflict",
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }

    /**
     * Update employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        log.info("PUT /employees/{} - Updating employee", id);

        try {
            Optional<EmployeeDTO> updatedEmployee = employeeService.updateEmployee(id, employeeDTO);

            if (updatedEmployee.isPresent()) {
                ApiResponse<EmployeeDTO> response = new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Employee updated successfully",
                        updatedEmployee.get()
                );
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            ApiResponse<EmployeeDTO> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "Employee not found",
                    "No employee found with ID: " + id
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            log.error("Error updating employee: {}", e.getMessage());
            ApiResponse<EmployeeDTO> response = new ApiResponse<>(
                    HttpStatus.CONFLICT.value(),
                    "Conflict",
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }

    /**
     * Delete employee (hard delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteEmployee(@PathVariable Long id) {
        log.info("DELETE /employees/{} - Deleting employee", id);

        boolean deleted = employeeService.deleteEmployee(id);

        if (deleted) {
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Employee deleted successfully",
                    "Employee with ID " + id + " has been deleted"
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                "Employee not found",
                "No employee found with ID: " + id
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Soft delete employee (mark as inactive)
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<EmployeeDTO>> softDeleteEmployee(@PathVariable Long id) {
        log.info("PATCH /employees/{}/deactivate - Soft deleting employee", id);

        Optional<EmployeeDTO> deactivatedEmployee = employeeService.softDeleteEmployee(id);

        if (deactivatedEmployee.isPresent()) {
            ApiResponse<EmployeeDTO> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Employee deactivated successfully",
                    deactivatedEmployee.get()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ApiResponse<EmployeeDTO> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                "Employee not found",
                "No employee found with ID: " + id
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Get total employee count
     */
    @GetMapping("/stats/total")
    public ResponseEntity<ApiResponse<Long>> getTotalEmployeeCount() {
        log.info("GET /employees/stats/total - Getting total employee count");
        long count = employeeService.getTotalEmployeeCount();
        ApiResponse<Long> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Total employee count retrieved successfully",
                count
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get active employee count
     */
    @GetMapping("/stats/active")
    public ResponseEntity<ApiResponse<Long>> getActiveEmployeeCount() {
        log.info("GET /employees/stats/active - Getting active employee count");
        long count = employeeService.getActiveEmployeeCount();
        ApiResponse<Long> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Active employee count retrieved successfully",
                count
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

