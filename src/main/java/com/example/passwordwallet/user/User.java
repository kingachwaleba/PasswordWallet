package com.example.passwordwallet.user;

import com.example.passwordwallet.password.Password;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank
    private String login;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    @NotBlank
    private String password;

    @Column
    private String salt;

    @Column(nullable = false)
    private Boolean isPasswordKeptAsHash;

    // @Transient annotation is used to indicate that a field is not to be persisted in the database
    @Transient
    private String roles = "ROLE_USER";

    @JsonIgnore
    @OneToMany(mappedBy="password", cascade = CascadeType.ALL)
    private Set<Password> passwordSet = new HashSet<>();

    public void addPassword(Password password) {
        passwordSet.add(password);
        password.setUser(this);
    }
}
