package io.github.xcvqqz.cloud_file_storage.service.storage;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequestDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponseDTO;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface FileStorageService {

    ResourceResponseDTO getResourceInfo(String path);

    ResourceResponseDTO deleteResource(ResourceRequestDTO request);

    void upload(String resourcePath, MultipartFile multipartFile);

    boolean bucketExist(String bucketName);

}
