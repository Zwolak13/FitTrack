package fit.track.demo.auth.dto;

public record ChangePasswordRequest(
        String oldPassword, String newPassword
) {}

