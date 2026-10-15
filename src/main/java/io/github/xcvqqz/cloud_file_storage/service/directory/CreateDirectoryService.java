package io.github.xcvqqz.cloud_file_storage.service.directory;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.DirectoryResponse;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.exception.ResourceAlreadyExistsException;
import io.github.xcvqqz.cloud_file_storage.resolver.ResourcePathResolver;
import io.github.xcvqqz.cloud_file_storage.storage.MinioStorage;
import io.github.xcvqqz.cloud_file_storage.validator.ResourcePathValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDirectoryService {

    private final MinioStorage minioStorage;
    private final ResourcePathResolver pathResolver;
    private final ResourcePathValidator pathValidator;

    public DirectoryResponse create(ResourceRequest request) {

        pathValidator.validate(request);

        String path = pathResolver.resolve(request);
        path = path.endsWith("/") ? path : path + "/";

        if (minioStorage.resourceExists(path)){
            throw new ResourceAlreadyExistsException("папка с именем " + path.substring(path.indexOf("/")+1) + " уже существует. Конфликт");
        }

        return minioStorage.createDirectory(path);

    }

}