package com.example.passwordwallet.login_attempt;

import com.example.passwordwallet.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Integer> {

    List<LoginAttempt> findAllByIsCorrectAndUserOrderByAttemptTimeDesc(Boolean isCorrect, User user);
}
