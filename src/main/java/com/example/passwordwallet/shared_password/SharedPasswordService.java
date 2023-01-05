package com.example.passwordwallet.shared_password;

import com.example.passwordwallet.password.Password;
import com.example.passwordwallet.user.User;

import java.util.List;

public interface SharedPasswordService {

    SharedPassword save(User user, User owner, Password password);
    List<SharedPassword> findAllByOwnerAndUserAndPassword(User owner, User user, Password password);
}
