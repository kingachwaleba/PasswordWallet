package com.example.passwordwallet.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class JwtResponse {

    private String token;
    private Date expiryDate;
    private String type = "Bearer";
    private String login;

    public JwtResponse(String accessToken, Date expiryDate, String login) {
        this.token = accessToken;
        this.expiryDate = expiryDate;
        this.login = login;
    }
}
