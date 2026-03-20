package com.actions.deployment.controller;

import com.actions.deployment.dto.EmployeeDTO;
import com.actions.deployment.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        objectMapper = new ObjectMapper();

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
    void testGetAllEmployees() throws Exception {
        // Arrange
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(employeeDTO));

        // Act & Assert
        mockMvc.perform(get("/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Employees retrieved successfully"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testGetAllActiveEmployees() throws Exception {
        // Arrange
        when(employeeService.getAllActiveEmployees()).thenReturn(Arrays.asList(employeeDTO));

        // Act & Assert
        mockMvc.perform(get("/employees/active")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(employeeService, times(1)).getAllActiveEmployees();
    }

    @Test
    void testGetEmployeeById() throws Exception {
        // Arrange
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employeeDTO));

        // Act & Assert
        mockMvc.perform(get("/employees/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.firstName").value("John"));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void testGetEmployeeByIdNotFound() throws Exception {
        // Arrange
        when(employeeService.getEmployeeById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/employees/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(employeeService, times(1)).getEmployeeById(999L);
    }

    @Test
    void testGetEmployeeByEmail() throws Exception {
        // Arrange
        when(employeeService.getEmployeeByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(employeeDTO));

        // Act & Assert
        mockMvc.perform(get("/employees/email/john.doe@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));

        verify(employeeService, times(1)).getEmployeeByEmail("john.doe@example.com");
    }

    @Test
    void testCreateEmployee() throws Exception {
        // Arrange
        when(employeeService.createEmployee(any(EmployeeDTO.class))).thenReturn(employeeDTO);

        // Act & Assert
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("Employee created successfully"));

        verify(employeeService, times(1)).createEmployee(any(EmployeeDTO.class));
    }

    @Test
    void testCreateEmployeeWithDuplicateEmail() throws Exception {
        // Arrange
        when(employeeService.createEmployee(any(EmployeeDTO.class)))
                .thenThrow(new IllegalArgumentException("Email already exists"));

        // Act & Assert
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        verify(employeeService, times(1)).createEmployee(any(EmployeeDTO.class));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        // Arrange
        employeeDTO.setFirstName("Jane");
        when(employeeService.updateEmployee(eq(1L), any(EmployeeDTO.class)))
                .thenReturn(Optional.of(employeeDTO));

        // Act & Assert
        mockMvc.perform(put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Employee updated successfully"));

        verify(employeeService, times(1)).updateEmployee(eq(1L), any(EmployeeDTO.class));
    }

    @Test
    void testUpdateEmployeeNotFound() throws Exception {
        // Arrange
        when(employeeService.updateEmployee(eq(999L), any(EmployeeDTO.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/employees/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(employeeService, times(1)).updateEmployee(eq(999L), any(EmployeeDTO.class));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        // Arrange
        when(employeeService.deleteEmployee(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/employees/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Employee deleted successfully"));

        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    void testDeleteEmployeeNotFound() throws Exception {
        // Arrange
        when(employeeService.deleteEmployee(999L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/employees/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(employeeService, times(1)).deleteEmployee(999L);
    }

    @Test
    void testSoftDeleteEmployee() throws Exception {
        // Arrange
        employeeDTO.setIsActive(false);
        when(employeeService.softDeleteEmployee(1L)).thenReturn(Optional.of(employeeDTO));

        // Act & Assert
        mockMvc.perform(patch("/employees/1/deactivate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Employee deactivated successfully"));

        verify(employeeService, times(1)).softDeleteEmployee(1L);
    }

    @Test
    void testGetTotalEmployeeCount() throws Exception {
        // Arrange
        when(employeeService.getTotalEmployeeCount()).thenReturn(1L);

        // Act & Assert
        mockMvc.perform(get("/employees/stats/total")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value(1));

        verify(employeeService, times(1)).getTotalEmployeeCount();
    }

    @Test
    void testGetActiveEmployeeCount() throws Exception {
        // Arrange
        when(employeeService.getActiveEmployeeCount()).thenReturn(1L);

        // Act & Assert
        mockMvc.perform(get("/employees/stats/active")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value(1));

        verify(employeeService, times(1)).getActiveEmployeeCount();
    }
}

