package io.github.xcvqqz.cloud_file_storage.dto.response.resource;


import io.github.xcvqqz.cloud_file_storage.entity.ResourceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ResourceResponseDTO {

    private String path;
    private ResourceType type;

}