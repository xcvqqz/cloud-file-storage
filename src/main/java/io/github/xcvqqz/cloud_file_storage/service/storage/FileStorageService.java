package io.github.xcvqqz.cloud_file_storage.service.storage;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    ResourceResponse getResourceInfo(String path);

    ResourceResponse deleteResource(ResourceRequest request);

    void upload(String resourcePath, MultipartFile multipartFile);

    boolean bucketExist(String bucketName);

}
