package org.example.stage5_2.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
   Lombok, boilerplate code reduction library, is used to generate the getters, setters, equals, hashcode, and toString methods.

   Jakarta Bean Validation - added to validate the fields of the Student class.
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {

    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotBlank(message = "First name is required")
    @NotBlank
    @NotEmpty
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Min(value = 0, message = "Age must be a positive number")
    private double age;
}