package com.example.passwordwallet.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class EnvConfig implements EnvironmentAware {

    private static Environment environment;

    @Override
    public void setEnvironment(final Environment environment) {
        EnvConfig.environment = environment;
    }

    public static String getSecretKey() {
        return environment.getProperty("app.secretKey");
    }

    public static String getPepper() {
        return environment.getProperty("app.pepper");
    }
}
