package com.example.passwordwallet.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank
    private String login;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    @NotBlank
    private String password;

    @Column
    private String salt;

    @Column(nullable = false)
    private Boolean isPasswordKeptAsHash;

    // @Transient annotation is used to indicate that a field is not to be persisted in the database
    @Transient
    private String roles = "ROLE_USER";

}
