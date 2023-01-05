package com.example.passwordwallet.shared_password;

import com.example.passwordwallet.password.Password;
import com.example.passwordwallet.user.User;

import java.util.List;
import java.util.Optional;

public interface SharedPasswordService {

    SharedPassword save(User user, User owner, Password password);
    Optional<SharedPassword> getOneById(int id);
    List<SharedPassword> findAllByUserAndPassword(User user, Password password);
    List<SharedPassword> findAllByOwnerAndUserAndPassword(User owner, User user, Password password);
    List<SharedPassword> findAllByUser(User user);

}
