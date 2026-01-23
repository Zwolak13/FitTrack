package fit.track.demo.controller;

import fit.track.demo.auth.dto.ChangePasswordRequest;
import fit.track.demo.auth.dto.UserDto;
import fit.track.demo.model.User;
import fit.track.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerUnitTest {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private UserController controller;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        controller = new UserController(userService, passwordEncoder);
    }

    @Test
    void me_shouldReturnUserDto_whenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setUsername("testuser");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        when(userService.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        ResponseEntity<UserDto> response = controller.me(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getId(), response.getBody().getId());
        assertEquals(user.getEmail(), response.getBody().getEmail());
        assertEquals(user.getUsername(), response.getBody().getUsername());
    }

    @Test
    void me_shouldThrowException_whenUserNotFound() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("missing@test.com");

        when(userService.findByEmail("missing@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> controller.me(auth)
        );
    }

    @Test
    void changePassword_shouldUpdatePassword_whenOldPasswordMatches() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("hashedOld");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        ChangePasswordRequest request =
                new ChangePasswordRequest("old123", "new123");

        when(userService.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old123", "hashedOld"))
                .thenReturn(true);
        when(passwordEncoder.encode("new123"))
                .thenReturn("hashedNew");

        ResponseEntity<?> response =
                controller.changePassword(auth, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("hashedNew", user.getPassword());
        verify(userService).save(user);
    }

    @Test
    void changePassword_shouldReturnUnauthorized_whenOldPasswordInvalid() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("hashedOld");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        ChangePasswordRequest request =
                new ChangePasswordRequest("wrong", "new123");

        when(userService.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashedOld"))
                .thenReturn(false);

        ResponseEntity<?> response =
                controller.changePassword(auth, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService, never()).save(any());
    }

    @Test
    void changePassword_shouldThrowException_whenUserNotFound() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("missing@test.com");

        ChangePasswordRequest request =
                new ChangePasswordRequest("old", "new");

        when(userService.findByEmail("missing@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> controller.changePassword(auth, request)
        );
    }
}

