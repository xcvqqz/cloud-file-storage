package io.github.xcvqqz.cloud_file_storage.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record ResourceRequest(

        @NotBlank(message = "path shouldn't empty or null")
        String path
)
{}