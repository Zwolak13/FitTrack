package fit.track.demo.service;

import fit.track.demo.model.User;
import fit.track.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceUnitTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        service = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void save_shouldEncodePasswordAndSaveUser() {
        // given
        when(passwordEncoder.encode("plainPassword"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // when
        User result = service.save("john", "john@test.com", "plainPassword");

        // then
        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("john@test.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findByEmail_shouldReturnUserWhenExists() {
        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        Optional<User> result = service.findByEmail("test@test.com");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository).findByEmail("test@test.com");
    }

    @Test
    void findByEmail_shouldReturnEmptyWhenNotFound() {
        when(userRepository.findByEmail("missing@test.com"))
                .thenReturn(Optional.empty());

        Optional<User> result = service.findByEmail("missing@test.com");

        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail("missing@test.com");
    }
}
