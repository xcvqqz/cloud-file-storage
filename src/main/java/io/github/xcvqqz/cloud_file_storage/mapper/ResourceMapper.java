package io.github.xcvqqz.cloud_file_storage.mapper;


import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.minio.StatObjectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceMapper {


    ResourceResponse statObjectToResourceResponseDTO(StatObjectResponse statObjectResponse);
    
}