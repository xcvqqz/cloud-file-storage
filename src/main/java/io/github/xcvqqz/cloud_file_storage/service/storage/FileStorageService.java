package io.github.xcvqqz.cloud_file_storage.service.storage;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.minio.errors.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface FileStorageService {

    ResourceResponse getResourceInfo(String path);

    void upload(String resourcePath, MultipartFile multipartFile);

    boolean bucketExist(String bucketName);

    Resource downloadFile(String path);

    Resource downloadDirectory(String path);
}
