package io.github.xcvqqz.cloud_file_storage.dto.response;

public record ResourceResponse(

        String path,
        String name,
        long size,
        String type

)
{}
