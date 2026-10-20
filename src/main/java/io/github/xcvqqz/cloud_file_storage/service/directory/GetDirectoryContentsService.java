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

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetDirectoryContentsService {

    private final MinioStorage minioStorage;
    private final ResourcePathResolver pathResolver;
    private final ResourcePathValidator pathValidator;

    public List<ResourceResponse> getContentsInfo(ResourceRequest request) {

        pathValidator.validate(request);

        String path = pathResolver.resolve(request);
        path = path.endsWith("/") ? path : path + "/";

        return minioStorage.getContentByDirectory(path);



    }


}
