package io.github.xcvqqz.cloud_file_storage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserAuthDTO(

        @NotBlank(message = "login shouldn't empty or null")
        @Size(min = 3, max = 30, message = "login should be min 3 and less 30 symbol")
        String name,

        @NotBlank(message = "password shouldn't empty or null")
        @Size(min = 3, max = 30, message = "Password should be min 3 and less 30 symbol")
        String password
) {}
