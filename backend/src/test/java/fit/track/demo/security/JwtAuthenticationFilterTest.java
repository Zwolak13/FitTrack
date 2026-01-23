package fit.track.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterUnitTest {

    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;
    private JwtAuthenticationFilter filter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        userDetailsService = mock(UserDetailsService.class);
        filter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSkipAuthentication_whenNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldSkipAuthentication_whenHeaderDoesNotStartWithBearer() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic abc");

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticate_whenTokenIsInvalid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer bad.token");
        when(jwtUtil.extractUsername("bad.token")).thenReturn("test@test.com");

        UserDetails userDetails =
                User.withUsername("test@test.com")
                        .password("secret")
                        .authorities("USER")
                        .build();

        when(userDetailsService.loadUserByUsername("test@test.com"))
                .thenReturn(userDetails);

        when(jwtUtil.isTokenValid("bad.token", userDetails))
                .thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldAuthenticate_whenTokenIsValid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtUtil.extractUsername("valid.token")).thenReturn("test@test.com");

        UserDetails userDetails =
                User.withUsername("test@test.com")
                        .password("secret")
                        .authorities("USER")
                        .build();

        when(userDetailsService.loadUserByUsername("test@test.com"))
                .thenReturn(userDetails);

        when(jwtUtil.isTokenValid("valid.token", userDetails))
                .thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(auth);
        assertEquals("test@test.com", auth.getName());
        assertTrue(auth.isAuthenticated());

        verify(filterChain).doFilter(request, response);
    }
}
