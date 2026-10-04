package com.altees.OpenAIExample.service;

import com.altees.OpenAIExample.model.Employee;
import com.altees.OpenAIExample.model.EmployeeMapper;
import com.altees.OpenAIExample.model.EmployeeRequest;
import com.altees.OpenAIExample.model.EmployeeResponse;
import com.altees.OpenAIExample.repostiory.EmployeeRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * EmployeeService
 * Service layer for business logic related to Employee entity
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * Create a new employee
     */
    @Tool(name = "createEmployee",description = "Create a new employee record with the given details")
    public EmployeeResponse createEmployee(@ToolParam(description = "The details of the employee to create") EmployeeRequest employeeRequest) {
        Employee employee = employeeMapper.requestToEntity(employeeRequest);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.entityToResponse(savedEmployee);
    }

    /**
     * Get all employees
     */
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get employee by ID
     */
    @Tool(name = "getEmployeeById", description = "Get employee details by ID",returnDirect = false)
    public EmployeeResponse getEmployeeById(@ToolParam(description = "Id to fetch employee record") Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::entityToResponse)
                .orElse(null);
    }

    /**
     * Get employee by name
     */
    public EmployeeResponse getEmployeeByName(String name) {
        Employee employee = employeeRepository.findByName(name);
        return employeeMapper.entityToResponse(employee);
    }

    /**
     * Update an existing employee
     */
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employeeMapper.updateEntityFromRequest(employeeRequest, employee);
                    Employee updatedEmployee = employeeRepository.save(employee);
                    return employeeMapper.entityToResponse(updatedEmployee);
                })
                .orElse(null);
    }

    /**
     * Delete an employee
     */
    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

