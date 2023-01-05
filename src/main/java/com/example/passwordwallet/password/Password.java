package com.example.passwordwallet.password;

import com.example.passwordwallet.shared_password.SharedPassword;
import com.example.passwordwallet.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "password")
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, length = 256)
    @NotBlank(message = "{password.password.notBlank}")
    private String password;

    @Column(length = 256)
    private String web_address;

    @Column(length = 256)
    private String description;

    @Column(length = 45)
    @Size(max = 45, message = "{password.login.size}")
    private String login;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName="id")
    @JsonIgnore
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy="password", cascade = CascadeType.ALL)
    private Set<SharedPassword> sharedPasswordSet = new HashSet<>();

    public void addSharedPassword(SharedPassword sharedPassword) {
        sharedPasswordSet.add(sharedPassword);
        sharedPassword.setPassword(this);
    }
}
