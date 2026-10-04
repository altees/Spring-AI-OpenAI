package com.altees.OpenAIExample.controller;

import com.altees.OpenAIExample.model.EmployeeRequest;
import com.altees.OpenAIExample.model.EmployeeResponse;
import com.altees.OpenAIExample.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * EmployeeController
 * REST API endpoints for Employee management
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Create a new employee
     * POST /api/employees
     */
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        EmployeeResponse response = employeeService.createEmployee(employeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all employees
     * GET /api/employees
     */
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    /**
     * Get employee by ID
     * GET /api/employees/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get employee by name
     * GET /api/employees/name/{name}
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<EmployeeResponse> getEmployeeByName(@PathVariable String name) {
        EmployeeResponse employee = employeeService.getEmployeeByName(name);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Update an existing employee
     * PUT /api/employees/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequest employeeRequest) {
        EmployeeResponse response = employeeService.updateEmployee(id, employeeRequest);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete an employee
     * DELETE /api/employees/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        if (employeeService.deleteEmployee(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}

