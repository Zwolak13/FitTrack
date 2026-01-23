package fit.track.demo.service;

import fit.track.demo.model.User;
import fit.track.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceUnitTest {

    private UserRepository userRepository;
    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        service = new CustomUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        // given
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("secret");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        // when
        UserDetails result = service.loadUserByUsername("test@test.com");

        // then
        assertNotNull(result);
        assertEquals("test@test.com", result.getUsername());
        assertEquals("secret", result.getPassword());
        assertTrue(
                result.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals("USER"))
        );

        verify(userRepository).findByEmail("test@test.com");
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        // given
        when(userRepository.findByEmail("missing@test.com"))
                .thenReturn(Optional.empty());

        // when + then
        assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing@test.com")
        );

        verify(userRepository).findByEmail("missing@test.com");
    }
}
