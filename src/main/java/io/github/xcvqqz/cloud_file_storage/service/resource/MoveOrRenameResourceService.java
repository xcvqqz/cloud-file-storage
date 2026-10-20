package io.github.xcvqqz.cloud_file_storage.service.resource;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.entity.ResourceType;
import io.github.xcvqqz.cloud_file_storage.exception.BadRequestException;
import io.github.xcvqqz.cloud_file_storage.exception.ResourceAlreadyExistsException;
import io.github.xcvqqz.cloud_file_storage.exception.ResourceNotFoundException;
import io.github.xcvqqz.cloud_file_storage.exception.StorageException;
import io.github.xcvqqz.cloud_file_storage.resolver.ResourcePathResolver;
import io.github.xcvqqz.cloud_file_storage.storage.MinioStorage;
import io.github.xcvqqz.cloud_file_storage.validator.ResourcePathValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoveOrRenameResourceService {

    private final MinioStorage minioStorage;
    private final ResourcePathResolver pathResolver;
    private final GetResourceInfoService getResourceInfoService;
    private final ResourcePathValidator resourcePathValidator;

    public ResourceResponse moveOrRename(ResourceRequest from, ResourceRequest to){

        resourcePathValidator.validate(from);
        resourcePathValidator.validate(to);

        String fromPath = pathResolver.resolve(from);
        String toPath = pathResolver.resolve(to);

        ResourceResponse response;

        if(fromPath.equals(toPath)){
            throw new BadRequestException("путь source и путь target должны быть разными");
        }

        ResourceResponse source = minioStorage.getResourceInfo(fromPath);

        if(minioStorage.resourceExists(toPath)){
            throw new ResourceAlreadyExistsException("The resource at path \"to\" already exists, conflict");
        }


        if(ResourceType.FILE.equals(source.getType())){
            response = minioStorage.moveFile(fromPath, toPath);
        } else if (ResourceType.DIRECTORY.equals(source.getType())){
            response = minioStorage.moveDirectory(fromPath, toPath);
        } else {
            throw new StorageException("не получилось осуществить перемещение или переименование объекта");
        }
        return response;
    }


}
