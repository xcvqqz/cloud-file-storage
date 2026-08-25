package io.github.xcvqqz.cloud_file_storage.dto.response.resource;


import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FileResponseDTO extends ResourceResponseDTO {

   private String name;
   private long size;

}