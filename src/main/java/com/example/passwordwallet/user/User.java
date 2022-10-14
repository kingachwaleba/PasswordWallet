package com.example.passwordwallet.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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

    @Column(unique = true, nullable = false, length = 30)
    @NotBlank
    private String login;

    @Column(unique = true, nullable = false, length = 30)
    @NotBlank
    private String email;

    //    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = 512, nullable = false)
    @NotBlank
    private String password;

//    @Column(length = 20)
//    private String salt;

//    private boolean isPasswordKeptAsHash;

    // @Transient annotation is used to indicate that a field is not to be persisted in the database
    @Transient
    private String roles = "ROLE_USER";

}
