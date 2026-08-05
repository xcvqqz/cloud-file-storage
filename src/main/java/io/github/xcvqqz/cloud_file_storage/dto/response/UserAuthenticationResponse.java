package io.github.xcvqqz.cloud_file_storage.dto.response;
;
import java.util.Set;

public record UserAuthenticationResponse(
        String name,
        Set<String> roles
)
{}
