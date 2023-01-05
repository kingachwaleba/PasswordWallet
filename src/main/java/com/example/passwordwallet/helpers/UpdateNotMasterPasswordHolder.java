package com.example.passwordwallet.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNotMasterPasswordHolder {

    @Column(nullable = false, length = 256)
    @NotBlank(message = "{password.password.notBlank}")
    private String password;

    @Column(length = 256)
    private String web_address;

    @Column(length = 256)
    private String description;

    @Column(length = 45)
    @Size(max = 45, message = "{password.login.size}")
    private String login;
}
