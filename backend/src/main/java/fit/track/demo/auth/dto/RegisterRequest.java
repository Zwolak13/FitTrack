package fit.track.demo.auth.dto;

public record RegisterRequest(
        String email, String username, String password
) {}
