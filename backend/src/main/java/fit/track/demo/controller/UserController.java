package fit.track.demo.controller;

import fit.track.demo.auth.dto.ChangePasswordRequest;
import fit.track.demo.auth.dto.UserDto;
import fit.track.demo.model.User;
import fit.track.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        String email = authentication.getName();

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(
                new UserDto(user.getId(), user.getEmail(), user.getUsername())
        );
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request
    ) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        //userService.save(user);

        return ResponseEntity.ok().build();
    }
}

