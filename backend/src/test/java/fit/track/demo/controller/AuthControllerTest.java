package fit.track.demo.controller;

import fit.track.demo.auth.dto.RegisterRequest;
import fit.track.demo.repository.UserRepository;
import fit.track.demo.security.JwtUtil;
import fit.track.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerUnitTest {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    private UserService userService;
    private AuthController controller;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtUtil = mock(JwtUtil.class);
        userRepository = mock(UserRepository.class);
        userService = mock(UserService.class);

        controller = new AuthController(
                authenticationManager,
                jwtUtil,
                userRepository,
                userService
        );
    }

    @Test
    void register_shouldSaveUser_whenEmailNotExists() {
        RegisterRequest request =
                new RegisterRequest("user", "test@test.com", "pass");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.register(request);

        assertEquals(200, response.getStatusCode().value());
        verify(userService).save(
                request.username(),
                request.email(),
                request.password()
        );
    }
}
