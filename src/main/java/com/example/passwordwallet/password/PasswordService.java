package com.example.passwordwallet.password;

import com.example.passwordwallet.helpers.UpdateNotMasterPasswordHolder;
import com.example.passwordwallet.shared_password.SharedPassword;
import com.example.passwordwallet.user.User;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PasswordService {

    Password save(Password password) throws Exception;
    Password edit(Password oldPassword, UpdateNotMasterPasswordHolder newPassword) throws Exception;
    void delete(int id);
    Optional<Password> findById(int id);
    List<Password> findAll() throws Exception;
    Map<String, Object> getAll() throws Exception;
    String getOne(int id) throws Exception;
    Optional<Password> getOneById(int id);
    List<String> getErrorList(BindingResult bindingResult);
    SharedPassword sharePassword(int userId, int passwordId);
}
