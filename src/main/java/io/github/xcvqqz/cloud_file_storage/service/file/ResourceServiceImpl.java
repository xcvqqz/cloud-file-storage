package io.github.xcvqqz.cloud_file_storage.service.file;

import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequestDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.FileResponseDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponseDTO;
import io.github.xcvqqz.cloud_file_storage.entity.ResourceType;
import io.github.xcvqqz.cloud_file_storage.service.auth.UserService;
import io.github.xcvqqz.cloud_file_storage.service.storage.MinioService;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final MinioService minioService;
    private final UserService userService;


    @Override
    public ResourceResponseDTO getResourceInfo(ResourceRequestDTO request) {
        String path = buildPath(request.path(), "");
        ResourceResponseDTO resourceResponse = minioService.getResourceInfo(path);
        return resourceResponse;
    }



    @Override
    public ResourceResponseDTO upload(ResourceRequestDTO request, MultipartFile file) {

        String path = request.path();

        if(!path.endsWith("/")){
              path += "/";
        }

        String objectPath = buildPath(path, file.getOriginalFilename());
        minioService.upload(objectPath, file);

        return FileResponseDTO.builder()
                .path(objectPath)
                .name(file.getName())
                .size(file.getSize())
                .type(ResourceType.FILE)
                .build();

    }

        //user-files
       // user-1-files
      //docs/test1.txt.
     //storage_folder/upload_folder/test.txt


    private String buildPath(String path, String resourceName) {

        Long currentUserId = userService.getCurrentUserId();

        return String.format("user-%d-files/%s/%s", currentUserId, path, resourceName);
    }
}










