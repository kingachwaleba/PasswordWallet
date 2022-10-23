package com.example.passwordwallet.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordHolder {

    @NotBlank(message = "{user.password.notBlank}")
    private String password;

    @Column(nullable = false)
    private Boolean isPasswordKeptAsHash;
}
