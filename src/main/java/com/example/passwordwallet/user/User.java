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
    @Pattern(regexp = "^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{1,30}$",
            message = "{user.login.regexp}")
    private String login;

    //    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = 512, nullable = false)
    @NotBlank
    private String password_hash;

    @Column(length = 20)
    private String salt;

    private boolean isPasswordKeptAsHash;

}
