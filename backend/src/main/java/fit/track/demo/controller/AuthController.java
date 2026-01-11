package fit.track.demo.controller;

import fit.track.demo.auth.dto.AuthResponse;
import fit.track.demo.repository.UserRepository;
import fit.track.demo.security.SecurityConfig;
import fit.track.demo.service.UserService;
import fit.track.demo.auth.dto.LoginRequest;
import fit.track.demo.auth.dto.RegisterRequest;
import fit.track.demo.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String accessToken = jwtUtil.generateAccessToken(request.email());
        String refreshToken = jwtUtil.generateRefreshToken(request.email());

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/auth/refresh")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        // sprawdzamy czy istnieje użytkownik
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity
                    .status(409)
                    .body("Email already exists");
        }

        // zapis użytkownika i hashowanie w serwisie
        userService.save(request.email(), request.password());

        // generujemy tokeny jak przy logowaniu
        String accessToken = jwtUtil.generateAccessToken(request.email());
        String refreshToken = jwtUtil.generateRefreshToken(request.email());

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/auth/refresh")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(new AuthResponse(accessToken));
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue("refreshToken") String refreshToken
    ) {
        String username = jwtUtil.extractUsername(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(username);
        return ResponseEntity.ok(new AuthResponse(newAccessToken));
    }
}

