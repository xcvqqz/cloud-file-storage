package io.github.xcvqqz.cloud_file_storage.service.resource;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.resolver.ResourcePathResolver;
import io.github.xcvqqz.cloud_file_storage.storage.MinioStorage;
import io.github.xcvqqz.cloud_file_storage.validator.ResourcePathValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DownloadResourceService {

    private final MinioStorage minioStorage;
    private final ResourcePathResolver pathResolver;
    private final ResourcePathValidator resourcePathValidator;

    public Resource download(ResourceRequest request) {

        resourcePathValidator.validate(request);

        String path = pathResolver.resolve(request);

        return path.endsWith("/")
                ? minioStorage.downloadDirectory(path)
                : minioStorage.downloadFile(path);
    }


}
