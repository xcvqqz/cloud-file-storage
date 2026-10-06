package io.github.xcvqqz.cloud_file_storage.service.resource;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.resolver.ResourcePathResolver;
import io.github.xcvqqz.cloud_file_storage.storage.MinioStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetResourceInfoService {

    private final MinioStorage minioStorage;
    private final ResourcePathResolver pathResolver;

    public ResourceResponse getResourceInfo(ResourceRequest request) {
        String path = pathResolver.resolve(request);
        return minioStorage.getResourceInfo(path);

    }

}
