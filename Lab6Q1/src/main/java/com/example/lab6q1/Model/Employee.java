package com.example.lab6q1.Model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class Employee {

    @NotEmpty( message = "Id must not be empty")
    @Size( min=3 , message = "Id Length must be more than 2 characters ")
    private String id;
    @NotEmpty( message = "Name must not be empty")
    @Size( min= 5 , message = "Name Length must be more than 4 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String name;
    @Email
    private String email;

    @Pattern(regexp = "^05[0-9]{8}$", message = "Phone number must start with 05 and 10 numbers")
    private String phoneNumber;


    @NotNull(message = "Age must not be null")
    @Positive(message = "Age must be a number")
    @Min(26)
    private int age;
    @NotEmpty(message = "Position must not be empty")
    @Pattern(regexp = "^(supervisor|coordinator)$" , message = "Position must be either \"supervisor\" or \"coordinator\" only")
    private String position;

    private boolean onLeave = false;
    @NotEmpty
//    @PastOrPresent
    @JsonFormat(pattern = "yyyy/MM/dd")
    private String hireDate;
    @NotNull(message = "Annual Leave must not be empty")
    @Positive(message = "Must be a positive number")
    private int annualLeave;




}
