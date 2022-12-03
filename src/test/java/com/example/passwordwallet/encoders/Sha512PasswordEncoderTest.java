package com.example.passwordwallet.encoders;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Sha512PasswordEncoderTest {

    @Test
    public void getPasswordWithSHA512_returnHashedPassword_ifEverythingWentCorrectly() {
        assertEquals(
                "9a48468867e5fb36a3ec567409a19e495cdb27494adf9dee2d72857e61164fd6fa629546178530bdb617755ea804d5d2d397cff8a9e3fff8403db216c89d21a8",
                Sha512PasswordEncoder.getPasswordWithSHA512("Kinga12345+"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPasswordWithSHA512_throwsIllegalArgumentException_ifGotEmptyString() {
        Sha512PasswordEncoder.getPasswordWithSHA512("");
    }
}