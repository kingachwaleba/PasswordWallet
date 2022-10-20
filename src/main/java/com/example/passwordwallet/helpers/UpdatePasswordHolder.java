package com.example.passwordwallet.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordHolder {

    @NotBlank(message = "{user.password.notBlank}")
    private String password;
}
