package io.github.xcvqqz.cloud_file_storage.service.file;

import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequestDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponseDTO;
import io.github.xcvqqz.cloud_file_storage.service.storage.MinioService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final MinioService minioService;

    @Override
    public ResourceResponseDTO getResourceInfo(ResourceRequestDTO request) {
        return null;
    }



}
