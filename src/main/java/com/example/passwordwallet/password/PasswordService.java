package com.example.passwordwallet.password;

import java.util.List;

public interface PasswordService {

    Password save(Password password) throws Exception;
    List<String> getAll() throws Exception;
}
