package com.example.passwordwallet.password;

import java.util.List;
import java.util.Optional;

public interface PasswordService {

    Password save(Password password) throws Exception;
    List<Password> getAll() throws Exception;
    String getOne(int id) throws Exception;
    Optional<Password> getOneById(int id);
}
