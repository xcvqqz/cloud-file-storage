package io.github.xcvqqz.cloud_file_storage.storage;


import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorage {

    ResourceResponse getResourceInfo(String path);

    void upload(String resourcePath, MultipartFile multipartFile);

    boolean bucketExist(String bucketName);

    Resource downloadFile(String path);

    boolean resourceExists(String resourcePath);

    Resource downloadDirectory(String path);
}
