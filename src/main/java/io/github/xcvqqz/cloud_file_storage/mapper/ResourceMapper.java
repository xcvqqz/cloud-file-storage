package io.github.xcvqqz.cloud_file_storage.mapper;


import java.util.*;

import io.github.xcvqqz.cloud_file_storage.dto.response.resource.DirectoryResponse;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.FileResponse;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.entity.ResourceType;
import io.github.xcvqqz.cloud_file_storage.exception.StorageException;
import io.minio.Result;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ResourceMapper {


    ResourceResponse ToResourceResponse(StatObjectResponse statObjectResponse);

    @Mapping(target = "name", source = "path", qualifiedByName = "fileName")
    @Mapping(target = "path", source = "path", qualifiedByName = "parentPath")
    @Mapping(target = "size", expression = "java(response.size())")
    @Mapping(target = "type", constant = "FILE")
    FileResponse toFileResponse(String path, StatObjectResponse response);

    @Mapping(target = "path", source = "path")
    @Mapping(target = "type", constant = "DIRECTORY")
    DirectoryResponse toDirectoryResponse(String path);

    @Named("fileName")
    default String getFileName(String path) {
        int lastSlash = path.lastIndexOf('/');
        return path.substring(lastSlash + 1);
    }

    @Named("parentPath")
    default String getParentPath(String path) {
        int lastSlash = path.lastIndexOf('/');
        return lastSlash < 0 ? "" : path.substring(0, lastSlash + 1);
    }

}

