package com.example.passwordwallet.password;

import com.example.passwordwallet.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository  extends JpaRepository<Password, Integer> {

    List<Password> findAllByUser(User user);
}
