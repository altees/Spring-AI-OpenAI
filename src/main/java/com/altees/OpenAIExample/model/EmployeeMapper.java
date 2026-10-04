package com.altees.OpenAIExample.model;

import org.springframework.stereotype.Component;

/**
 * EmployeeMapper
 * Helper class for mapping between Entity, Request, and Response objects
 */
@Component
public class EmployeeMapper {

    /**
     * Convert EmployeeRequest DTO to Employee Entity
     */
    public Employee requestToEntity(EmployeeRequest request) {
        if (request == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setDesignation(request.getDesignation());
        return employee;
    }

    /**
     * Convert Employee Entity to EmployeeResponse DTO
     */
    public EmployeeResponse entityToResponse(Employee employee) {
        if (employee == null) {
            return null;
        }
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getDesignation()
        );
    }

    /**
     * Convert EmployeeRequest DTO to Employee Entity (for updates)
     */
    public Employee updateEntityFromRequest(EmployeeRequest request, Employee employee) {
        if (request == null) {
            return employee;
        }
        if (request.getName() != null) {
            employee.setName(request.getName());
        }
        if (request.getDesignation() != null) {
            employee.setDesignation(request.getDesignation());
        }
        return employee;
    }

}

