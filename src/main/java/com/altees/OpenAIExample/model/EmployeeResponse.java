package com.altees.OpenAIExample.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmployeeResponse DTO
 * Used for returning employee data in API responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private Long id;
    private String name;
    private String designation;

}

