package com.example.passwordwallet;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        com.example.passwordwallet.encoders.Sha512PasswordEncoderTest.class,
        com.example.passwordwallet.encoders.Sha512PasswordEncoderTest.class,
        com.example.passwordwallet.utils.SecureUtilsTest.class,
})
@SpringBootTest
class PasswordWalletApplicationTests {

}
