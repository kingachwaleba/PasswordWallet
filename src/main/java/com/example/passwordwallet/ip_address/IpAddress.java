package com.example.passwordwallet.ip_address;

import com.example.passwordwallet.login_attempt.LoginAttempt;
import com.example.passwordwallet.password.Password;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ip_address")
public class IpAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private LocalDateTime tempLockTime;

    @Column(nullable = false)
    private Boolean isPermLock;

    @Column
    private String ipAddress;

    @Column
    private int unsuccessfulLoginCount;

    @JsonIgnore
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private Set<LoginAttempt> loginAttemptSet = new HashSet<>();

    public void addLoginAttempt(LoginAttempt loginAttempt) {
        loginAttemptSet.add(loginAttempt);
        loginAttempt.setIpAddress(this);
    }
}
