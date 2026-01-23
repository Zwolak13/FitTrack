package fit.track.demo.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private final String secret = "01234567890123456789012345678901"; // 32 bytes
    private final long accessExpiration = 1000L * 60; // 1 min
    private final long refreshExpiration = 1000L * 60 * 5; // 5 min

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret, accessExpiration, refreshExpiration);
    }

    @Test
    void generateAccessToken_shouldBeValid() {
        String username = "test@test.com";
        String token = jwtUtil.generateAccessToken(username);

        assertNotNull(token);
        assertEquals(username, jwtUtil.extractUsername(token));
    }

    @Test
    void generateRefreshToken_shouldBeValid() {
        String username = "user";
        String token = jwtUtil.generateRefreshToken(username);

        assertNotNull(token);
        assertEquals(username, jwtUtil.extractUsername(token));
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        UserDetails user = User.withUsername("alice").password("pwd").authorities("USER").build();
        String token = jwtUtil.generateAccessToken("alice");

        assertTrue(jwtUtil.isTokenValid(token, user));
    }

    @Test
    void isTokenValid_shouldReturnFalseForWrongUsername() {
        UserDetails user = User.withUsername("bob").password("pwd").authorities("USER").build();
        String token = jwtUtil.generateAccessToken("alice");

        assertFalse(jwtUtil.isTokenValid(token, user));
    }

    @Test
    void isTokenValid_shouldReturnFalseForExpiredToken() throws InterruptedException {
        JwtUtil shortJwtUtil = new JwtUtil(secret, 50, refreshExpiration); // 50 ms
        UserDetails user = User.withUsername("alice").password("pwd").authorities("USER").build();
        String token = shortJwtUtil.generateAccessToken("alice");

        // poczekaj aż token wygaśnie
        TimeUnit.MILLISECONDS.sleep(60);

        boolean valid;
        try {
            valid = shortJwtUtil.isTokenValid(token, user);
        } catch (ExpiredJwtException e) {
            valid = false;
        }

        assertFalse(valid);
    }

}
