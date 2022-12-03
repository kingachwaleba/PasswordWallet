package com.example.passwordwallet.encoders;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HMACPasswordEncoderTest {

    @Test
    public void getPasswordWithHMAC_returnHashedPassword_ifEverythingWentCorrectly() {
        assertEquals(
                "47cff69c4f0dc969ae4e33e6835b32d324cb6e61ab8b3ee195accc75efbf13cd2274cff97d912457490fc043831acb4f24adb0a065afa6f8b778ace69826eb84",
                HMACPasswordEncoder.getPasswordWithHMAC("Kinga12345+"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPasswordWithHMAC_throwsIllegalArgumentException_ifGotEmptyString() {
        HMACPasswordEncoder.getPasswordWithHMAC("");
    }
}