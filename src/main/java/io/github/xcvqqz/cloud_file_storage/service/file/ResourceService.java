package io.github.xcvqqz.cloud_file_storage.service.file;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {

    ResourceResponse getResourceInfo(ResourceRequest request);

    ResourceResponse upload(ResourceRequest requestDTO, MultipartFile multipartFile);

}