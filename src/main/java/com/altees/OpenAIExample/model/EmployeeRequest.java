package com.altees.OpenAIExample.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmployeeRequest DTO
 * Used for creating and updating employee records
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    private String name;
    private String designation;

}

