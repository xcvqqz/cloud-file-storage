package io.github.xcvqqz.cloud_file_storage.service.file;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequestDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponseDTO;
import io.github.xcvqqz.cloud_file_storage.service.storage.MinioService;

public interface ResourceService {


    ResourceResponseDTO getResourceInfo (ResourceRequestDTO request);


}