package io.github.xcvqqz.cloud_file_storage.dto;

public record UserRegistrationDTO(
        String name,
        String password,
        String confirmPassword)
{}