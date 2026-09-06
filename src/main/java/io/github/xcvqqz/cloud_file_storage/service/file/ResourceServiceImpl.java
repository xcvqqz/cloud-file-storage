package io.github.xcvqqz.cloud_file_storage.service.file;

import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.FileResponse;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.entity.ResourceType;
import io.github.xcvqqz.cloud_file_storage.resolver.ResourcePathResolver;
import io.github.xcvqqz.cloud_file_storage.service.auth.UserService;
import io.github.xcvqqz.cloud_file_storage.service.storage.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final MinioService minioService;
    private final ResourcePathResolver pathResolver;


    @Override
    public ResourceResponse getResourceInfo(ResourceRequest request) {

        String path = pathResolver.resolve(request);

        return minioService.getResourceInfo(path);

    }



    @Override
    public ResourceResponse upload(ResourceRequest request, MultipartFile file) {

        String path = pathResolver.resolve(request, file);

        minioService.upload(path, file);

        return FileResponse.builder()
                .path(request.path())
                .name(file.getOriginalFilename())
                .size(file.getSize())
                .type(ResourceType.FILE)
                .build();

    }

}










