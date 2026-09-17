package io.github.xcvqqz.cloud_file_storage.service.file;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface ResourceService {

    ResourceResponse getResourceInfo(ResourceRequest request);

    ResourceResponse upload(ResourceRequest request, MultipartFile multipartFile);

    Resource download(ResourceRequest request);

}