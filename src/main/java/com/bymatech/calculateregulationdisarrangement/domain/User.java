package com.bymatech.calculateregulationdisarrangement.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    @NotBlank
    @Size(min = 2, max = 20)
    private String userName;

    @NotBlank
    @Column(unique = true)
    @Email(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
    private String userEmail;

    @NotNull(message = "password should not be blank")
    private String userPass;

    private boolean enable;
    private String role;

    @Column(length = 500)
    private String userAbout;
}