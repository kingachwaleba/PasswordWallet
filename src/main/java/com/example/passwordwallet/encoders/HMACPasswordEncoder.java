package com.example.passwordwallet.encoders;

import com.example.passwordwallet.config.EnvConfig;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

public class HMACPasswordEncoder implements PasswordEncoder {

    private static final String secretKey = "2s5v8y/B?E(H+MbPeShVmYq3t6w9z$C&";

    @Override
    public String encode(CharSequence rawPassword) {
        return getPasswordWithHMAC(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(encode(rawPassword));
    }

    public static String getPasswordWithHMAC(String text){
        if (text.equals(""))
            throw new IllegalArgumentException();

        return new HmacUtils("HmacSHA512", secretKey).hmacHex(text);
    }
}
