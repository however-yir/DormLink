package com.example.springboot.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    @Test
    void shouldGenerateAndParseToken() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("DormLinkJwtSecretKey-2023-Interview-Hardening-ChangeMe!");
        properties.setExpirationSeconds(3600);
        JwtUtil jwtUtil = new JwtUtil(properties);

        String token = jwtUtil.generateToken("admin", "admin", "admin", "系统管理员", "/files/avatar1.jpg");

        Assertions.assertTrue(jwtUtil.validateToken(token));
        Assertions.assertEquals("admin", jwtUtil.getUsernameFromToken(token));
        Assertions.assertEquals("admin", jwtUtil.getRoleFromToken(token));
        Assertions.assertEquals("admin", jwtUtil.getUserIdFromToken(token));
        Assertions.assertEquals("系统管理员", jwtUtil.getDisplayNameFromToken(token));
    }

    @Test
    void shouldRejectMalformedToken() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("DormLinkJwtSecretKey-2023-Interview-Hardening-ChangeMe!");
        properties.setExpirationSeconds(3600);
        JwtUtil jwtUtil = new JwtUtil(properties);

        Assertions.assertFalse(jwtUtil.validateToken("invalid.token.value"));
    }
}
