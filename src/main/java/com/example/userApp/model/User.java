package com.example.userApp.model;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty(message = "Should not be empty")
    @Size(min = 5,max = 20, message = "The size must be between 5 and 20")
    private String name;
    @NotEmpty(message = "Should not be empty")
    @Email(message = "Email format not valid")
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(value = TemporalType.DATE)
    @NotNull(message = "Should not be empty")
    private Date dateOfBirth;
    private String photo;

}
