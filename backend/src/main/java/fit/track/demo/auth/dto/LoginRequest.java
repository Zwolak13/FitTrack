package fit.track.demo.auth.dto;

public record LoginRequest(
        String email,
        String password
) {}