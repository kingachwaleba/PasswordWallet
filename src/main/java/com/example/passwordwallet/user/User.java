package com.example.passwordwallet.user;

import com.example.passwordwallet.login_attempt.LoginAttempt;
import com.example.passwordwallet.password.Password;
import com.example.passwordwallet.shared_password.SharedPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true, nullable = false, length = 45)
    @Size(min = 5, max = 45, message = "{user.login.size}")
    @NotBlank(message = "{user.login.notBlank}")
    @Pattern(regexp = "^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{4,45}$",
            message = "{user.login.regexp}")
    private String login;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 1000)
    @NotBlank(message = "{user.password.notBlank}")
    private String password;

    @Column
    private String salt;

    @Column(nullable = false)
    private Boolean isPasswordKeptAsHash;

    @Column
    private LocalDateTime lockoutTime;

    // @Transient annotation is used to indicate that a field is not to be persisted in the database
    @Transient
    private String roles = "ROLE_USER";

    @Column
    private int unsuccessfulLoginCount;

    @JsonIgnore
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private Set<Password> passwordSet = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private Set<SharedPassword> sharedPasswordSet = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private Set<LoginAttempt> loginAttemptSet = new HashSet<>();

    public void addPassword(Password password) {
        passwordSet.add(password);
        password.setUser(this);
    }

    public void addLoginAttempt(LoginAttempt loginAttempt) {
        loginAttemptSet.add(loginAttempt);
        loginAttempt.setUser(this);
    }

    public void addSharedPassword(SharedPassword sharedPassword) {
        sharedPasswordSet.add(sharedPassword);
        sharedPassword.setUser(this);
    }
}
