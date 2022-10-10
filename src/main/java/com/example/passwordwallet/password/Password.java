package com.example.passwordwallet.password;

import com.example.passwordwallet.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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

    @Column(unique = true, nullable = false, length = 256)
    @NotBlank
    private String password;

    @Column(length = 256)
    private String web_address;

    @Column(length = 256)
    private String description;

    @Column(length = 30)
    private String login;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName="id")
    private User id_user;

}