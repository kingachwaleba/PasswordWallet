package com.example.passwordwallet.shared_password;

import com.example.passwordwallet.password.Password;
import com.example.passwordwallet.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharedPasswordRepository extends JpaRepository<SharedPassword, Integer> {

    List<SharedPassword> findAllByUserAndPassword(User user, Password password);
    List<SharedPassword> findAllByOwnerAndUserAndPassword(User owner, User user, Password password);
    List<SharedPassword> findAllByUser(User user);
    List<SharedPassword> findAllByPassword(Password password);
}
