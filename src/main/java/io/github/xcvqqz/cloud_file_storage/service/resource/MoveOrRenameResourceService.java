package io.github.xcvqqz.cloud_file_storage.service.resource;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.entity.ResourceType;
import io.github.xcvqqz.cloud_file_storage.exception.ResourceAlreadyExistsException;
import io.github.xcvqqz.cloud_file_storage.exception.ResourceNotFoundException;
import io.github.xcvqqz.cloud_file_storage.exception.StorageException;
import io.github.xcvqqz.cloud_file_storage.resolver.ResourcePathResolver;
import io.github.xcvqqz.cloud_file_storage.storage.MinioStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoveOrRenameResourceService {

    private final MinioStorage minioStorage;
    private final ResourcePathResolver pathResolver;

    public ResourceResponse moveOrRename(ResourceRequest from, ResourceRequest to){

        //сюда бы валидацию еще!!

        String fromPath = pathResolver.resolve(from);
        String toPath = pathResolver.resolve(to);

        if (!minioStorage.resourceExists(fromPath)) {
            throw new ResourceNotFoundException(
                    "Resource at path '" + from.path() + "' was not found"
            );
        }

        if(minioStorage.resourceExists(toPath)){
            throw new ResourceAlreadyExistsException("The resource at path \"to\" already exists, conflict");
        }

        ResourceType typeFrom = minioStorage.getResourceInfo(fromPath).getType();


        if(ResourceType.FILE.equals(typeFrom)){
            minioStorage.moveFile(fromPath, toPath);
            return buildFileResponse();
        } else if (ResourceType.DIRECTORY.equals(typeFrom)){
            minioStorage.moveDirectory(fromPath, toPath);
            return buildDirectoryResponse();
        } else {
            throw new StorageException("не получилось осуществить перемещение или переименование объекта");
        }
    }


}
