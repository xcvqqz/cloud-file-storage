package io.github.xcvqqz.cloud_file_storage.service.file;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequestDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponseDTO;
import io.github.xcvqqz.cloud_file_storage.service.storage.MinioService;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ResourceService {

    ResourceResponseDTO getResourceInfo(ResourceRequestDTO request);

    ResourceResponseDTO upload(ResourceRequestDTO requestDTO, MultipartFile multipartFile);

}