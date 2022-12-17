package com.example.passwordwallet.login_attempt;

import com.example.passwordwallet.ip_address.IpAddress;
import com.example.passwordwallet.password.Password;
import com.example.passwordwallet.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "login_attempt")
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private LocalDateTime attemptTime;

    @Column(nullable = false)
    private Boolean isCorrect;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName="id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ip_address_id", referencedColumnName="id")
    @JsonIgnore
    private IpAddress ipAddress;

    public LoginAttempt(LocalDateTime attemptTime, Boolean isCorrect, User user) {
        this.attemptTime = attemptTime;
        this.isCorrect = isCorrect;
        this.user = user;
    }
}