package com.example.passwordwallet.login_attempt;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Integer> {
}
