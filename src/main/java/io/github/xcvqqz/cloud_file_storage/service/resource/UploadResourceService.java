package io.github.xcvqqz.cloud_file_storage.service.resource;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.FileResponse;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.entity.ResourceType;
import io.github.xcvqqz.cloud_file_storage.resolver.ResourcePathResolver;
import io.github.xcvqqz.cloud_file_storage.storage.MinioStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadResourceService {

    private final ResourcePathResolver pathResolver;
    private final MinioStorage minioStorage;

    public ResourceResponse upload(ResourceRequest request, MultipartFile file) {

        String path = pathResolver.resolve(request, file);

        minioStorage.upload(path, file);

        return FileResponse.builder()
                .path(request.path())
                .name(file.getOriginalFilename())
                .size(file.getSize())
                .type(ResourceType.FILE)
                .build();
    }


}