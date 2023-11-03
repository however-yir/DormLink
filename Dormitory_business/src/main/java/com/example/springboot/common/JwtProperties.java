package com.example.springboot.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private long expirationSeconds;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    public void setExpirationSeconds(long expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
    }
}
