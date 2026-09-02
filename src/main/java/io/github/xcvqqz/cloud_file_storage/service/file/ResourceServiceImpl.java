package io.github.xcvqqz.cloud_file_storage.service.file;

import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequestDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponseDTO;
import io.github.xcvqqz.cloud_file_storage.service.auth.UserService;
import io.github.xcvqqz.cloud_file_storage.service.storage.MinioService;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final MinioService minioService;
    private final UserService userService;


    public ResourceResponseDTO getResourceInfo(ResourceRequestDTO request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Long currentUserId = userService.getCurrentUserId();
        String path = buildPath(request.path(), currentUserId);
        ResourceResponseDTO resourceResponse = minioService.getResourceInfo(path);
        return resourceResponse;
    }


    public




    private String buildPath(String path, Long userId) {
        return String.format("user-%d-files/%s", userId, path);
    }
}










