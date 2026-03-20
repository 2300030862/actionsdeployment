package com.actions.deployment.service;

import com.actions.deployment.dto.EmployeeDTO;
import com.actions.deployment.entity.Employee;
import com.actions.deployment.repository.EmployeeRepository;
import com.actions.deployment.service.impl.EmployeeServiceImpl;
import com.actions.deployment.util.EmployeeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPhone("1234567890");
        employee.setDepartment("IT");
        employee.setPosition("Developer");
        employee.setSalary(50000.0);
        employee.setHireDate(LocalDateTime.now());
        employee.setIsActive(true);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setPhone("1234567890");
        employeeDTO.setDepartment("IT");
        employeeDTO.setPosition("Developer");
        employeeDTO.setSalary(50000.0);
        employeeDTO.setHireDate(LocalDateTime.now());
        employeeDTO.setIsActive(true);
        employeeDTO.setCreatedAt(LocalDateTime.now());
        employeeDTO.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAllEmployees() {
        // Arrange
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.toDTO(any(Employee.class))).thenReturn(employeeDTO);

        // Act
        List<EmployeeDTO> result = employeeService.getAllEmployees();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetAllActiveEmployees() {
        // Arrange
        List<Employee> activeEmployees = Arrays.asList(employee);
        when(employeeRepository.findByIsActiveTrue()).thenReturn(activeEmployees);
        when(employeeMapper.toDTO(any(Employee.class))).thenReturn(employeeDTO);

        // Act
        List<EmployeeDTO> result = employeeService.getAllActiveEmployees();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsActive());
        verify(employeeRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    void testGetEmployeeById() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDTO(any(Employee.class))).thenReturn(employeeDTO);

        // Act
        Optional<EmployeeDTO> result = employeeService.getEmployeeById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        // Arrange
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTO> result = employeeService.getEmployeeById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(999L);
    }

    @Test
    void testGetEmployeeByEmail() {
        // Arrange
        when(employeeRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(employee));
        when(employeeMapper.toDTO(any(Employee.class))).thenReturn(employeeDTO);

        // Act
        Optional<EmployeeDTO> result = employeeService.getEmployeeByEmail("john.doe@example.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("john.doe@example.com", result.get().getEmail());
        verify(employeeRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testCreateEmployee() {
        // Arrange
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeMapper.toEntity(any(EmployeeDTO.class))).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toDTO(any(Employee.class))).thenReturn(employeeDTO);

        // Act
        EmployeeDTO result = employeeService.createEmployee(employeeDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testCreateEmployeeWithDuplicateEmail() {
        // Arrange
        when(employeeRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            employeeService.createEmployee(employeeDTO);
        });
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee() {
        // Arrange
        employeeDTO.setFirstName("Jane");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toDTO(any(Employee.class))).thenReturn(employeeDTO);

        // Act
        Optional<EmployeeDTO> result = employeeService.updateEmployee(1L, employeeDTO);

        // Assert
        assertTrue(result.isPresent());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployee() {
        // Arrange
        when(employeeRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = employeeService.deleteEmployee(1L);

        // Assert
        assertTrue(result);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEmployeeNotFound() {
        // Arrange
        when(employeeRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = employeeService.deleteEmployee(999L);

        // Assert
        assertFalse(result);
        verify(employeeRepository, times(0)).deleteById(999L);
    }

    @Test
    void testSoftDeleteEmployee() {
        // Arrange
        employee.setIsActive(false);
        employeeDTO.setIsActive(false);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toDTO(any(Employee.class))).thenReturn(employeeDTO);

        // Act
        Optional<EmployeeDTO> result = employeeService.softDeleteEmployee(1L);

        // Assert
        assertTrue(result.isPresent());
        assertFalse(result.get().getIsActive());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testGetTotalEmployeeCount() {
        // Arrange
        when(employeeRepository.count()).thenReturn(1L);

        // Act
        long result = employeeService.getTotalEmployeeCount();

        // Assert
        assertEquals(1L, result);
        verify(employeeRepository, times(1)).count();
    }

    @Test
    void testGetActiveEmployeeCount() {
        // Arrange
        List<Employee> activeEmployees = Arrays.asList(employee);
        when(employeeRepository.findByIsActiveTrue()).thenReturn(activeEmployees);

        // Act
        long result = employeeService.getActiveEmployeeCount();

        // Assert
        assertEquals(1L, result);
        verify(employeeRepository, times(1)).findByIsActiveTrue();
    }
}

