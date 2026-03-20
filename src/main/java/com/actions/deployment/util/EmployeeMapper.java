package com.actions.deployment.util;

import com.actions.deployment.dto.EmployeeDTO;
import com.actions.deployment.entity.Employee;
import org.springframework.stereotype.Component;

/**
 * Mapper utility for converting between Employee entity and EmployeeDTO
 */
@Component
public class EmployeeMapper {

    /**
     * Convert Employee entity to EmployeeDTO
     * @param employee the employee entity
     * @return the employee DTO
     */
    public EmployeeDTO toDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setDepartment(employee.getDepartment());
        dto.setPosition(employee.getPosition());
        dto.setSalary(employee.getSalary());
        dto.setHireDate(employee.getHireDate());
        dto.setIsActive(employee.getIsActive());
        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());

        return dto;
    }

    /**
     * Convert EmployeeDTO to Employee entity
     * @param dto the employee DTO
     * @return the employee entity
     */
    public Employee toEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }

        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setDepartment(dto.getDepartment());
        employee.setPosition(dto.getPosition());
        employee.setSalary(dto.getSalary());
        employee.setHireDate(dto.getHireDate());
        employee.setIsActive(dto.getIsActive());
        employee.setCreatedAt(dto.getCreatedAt());
        employee.setUpdatedAt(dto.getUpdatedAt());

        return employee;
    }
}

