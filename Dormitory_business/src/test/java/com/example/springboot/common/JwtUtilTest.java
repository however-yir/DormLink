package com.example.springboot.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilTest {

    @Test
    void shouldGenerateAndParseToken() {
        JwtUtil jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "DormLinkJwtSecretKey-2023-Interview-Hardening-ChangeMe!");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 3600_000L);

        String token = jwtUtil.generateToken("admin", "admin", "admin", "系统管理员", "/files/avatar1.jpg");

        Assertions.assertTrue(jwtUtil.validateToken(token));
        Assertions.assertEquals("admin", jwtUtil.getUsernameFromToken(token));
        Assertions.assertEquals("admin", jwtUtil.getRoleFromToken(token));
        Assertions.assertEquals("admin", jwtUtil.getUserIdFromToken(token));
        Assertions.assertEquals("系统管理员", jwtUtil.getDisplayNameFromToken(token));
    }

    @Test
    void shouldRejectMalformedToken() {
        JwtUtil jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "DormLinkJwtSecretKey-2023-Interview-Hardening-ChangeMe!");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 3600_000L);

        Assertions.assertFalse(jwtUtil.validateToken("invalid.token.value"));
    }
}
