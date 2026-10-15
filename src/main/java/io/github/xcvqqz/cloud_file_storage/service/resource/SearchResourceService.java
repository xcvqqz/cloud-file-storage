package io.github.xcvqqz.cloud_file_storage.service.resource;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import io.github.xcvqqz.cloud_file_storage.resolver.ResourcePathResolver;
import io.github.xcvqqz.cloud_file_storage.service.auth.UserService;
import io.github.xcvqqz.cloud_file_storage.storage.MinioStorage;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchResourceService {

    private final MinioStorage minioStorage;
    private final ResourcePathResolver pathResolver;

    private final UserService userService;


    public List<ResourceResponse> search(ResourceRequest request) {

        String requestPath = request.path();

        return minioStorage.searchResources(requestPath, getResolvePrefix());

    }

    private String getResolvePrefix(){
        return String.format("user-%d-files/", userService.getCurrentUserId());
    }



}
