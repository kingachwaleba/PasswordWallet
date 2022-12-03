package com.example.passwordwallet.utils;

import com.example.passwordwallet.encoders.Sha512PasswordEncoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecureUtilsTest {

    @Test
    public void encrypt_returnCorrectEncryptedString_ifEverythingWentCorrectly() throws Exception {
        String passwordToEncrypt = "Kinga12345+";
        String key = "47cff69c4f0dc969ae4e33e6835b32d324cb6e61ab8b3ee195accc75efbf13cd2274cff97d912457490fc043831acb4f24adb0a065afa6f8b778ace69826eb84";
        String encryptedData = SecureUtils.encrypt(passwordToEncrypt, SecureUtils.generateKey(key));
        assertEquals("l5HpDx3r42yeGXh2Gh0EhQ==", encryptedData);
    }

    @Test
    public void decrypt_returnCorrectDecryptedString_ifEverythingWentCorrectly() throws Exception {
        String encryptedPassword = "l5HpDx3r42yeGXh2Gh0EhQ==";
        String key = "47cff69c4f0dc969ae4e33e6835b32d324cb6e61ab8b3ee195accc75efbf13cd2274cff97d912457490fc043831acb4f24adb0a065afa6f8b778ace69826eb84";
        String decryptedData = SecureUtils.decrypt(encryptedPassword, SecureUtils.generateKey(key));
        assertEquals("Kinga12345+", decryptedData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encrypt_throwsIllegalArgumentException_ifGotEmptyData() throws Exception {
        String key = "47cff69c4f0dc969ae4e33e6835b32d324cb6e61ab8b3ee195accc75efbf13cd2274cff97d912457490fc043831acb4f24adb0a065afa6f8b778ace69826eb84";
        SecureUtils.encrypt("", SecureUtils.generateKey(key));
    }

    @Test(expected = IllegalArgumentException.class)
    public void encrypt_throwsIllegalArgumentException_ifGotNullKey() throws Exception {
        String passwordToEncrypt = "Kinga12345+";
        String key = "47cff69c4f0dc969ae4e33e6835b32d324cb6e61ab8b3ee195accc75efbf13cd2274cff97d912457490fc043831acb4f24adb0a065afa6f8b778ace69826eb84";
        String encryptedData = SecureUtils.encrypt(passwordToEncrypt, SecureUtils.generateKey(key));
        SecureUtils.encrypt(encryptedData, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decrypt_throwsIllegalArgumentException_ifGotEmptyData() throws Exception {
        String key = "47cff69c4f0dc969ae4e33e6835b32d324cb6e61ab8b3ee195accc75efbf13cd2274cff97d912457490fc043831acb4f24adb0a065afa6f8b778ace69826eb84";
        SecureUtils.decrypt("", SecureUtils.generateKey(key));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decrypt_throwsIllegalArgumentException_ifGotNullKey() throws Exception {
        String passwordToEncrypt = "Kinga12345+";
        String key = "47cff69c4f0dc969ae4e33e6835b32d324cb6e61ab8b3ee195accc75efbf13cd2274cff97d912457490fc043831acb4f24adb0a065afa6f8b778ace69826eb84";
        String encryptedData = SecureUtils.encrypt(passwordToEncrypt, SecureUtils.generateKey(key));
        SecureUtils.decrypt(encryptedData, null);
    }
}