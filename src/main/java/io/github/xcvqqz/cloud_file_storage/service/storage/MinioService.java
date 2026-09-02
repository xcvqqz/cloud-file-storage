package io.github.xcvqqz.cloud_file_storage.service.storage;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequestDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.DirectoryResponseDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.FileResponseDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponseDTO;
import io.github.xcvqqz.cloud_file_storage.exception.ResourceNotFoundException;
import io.github.xcvqqz.cloud_file_storage.exception.StorageException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

import static io.github.xcvqqz.cloud_file_storage.entity.ResourceType.DIRECTORY;
import static io.github.xcvqqz.cloud_file_storage.entity.ResourceType.FILE;


@RequiredArgsConstructor
@Service
public class MinioService implements FileStorageService {

    private static final String BUCKET_NAME = "user-files";

    private static final String DIRECTORY_NOT_FOUND_MESSAGE =
            "The folder at the specified path was not found." +
                    " Please check that the folder exists or that the path was entered correctly";

    private static final String FILE_NOT_FOUND_MESSAGE = "The file at the specified path was not found. " +
            "Please check that the file exists or that the path was entered correctly";

    private final MinioClient minioClient;


    @Override
    public ResourceResponseDTO getResourceInfo(String path) {
//        int index = path.lastIndexOf('/');
//        String result = path.substring(index + 1);

        return path.endsWith("/") ?
                getDirectoryInfo(path) :
                getFileInfo(path);
    }

    @Override
    public ResourceResponseDTO deleteResource(ResourceRequestDTO request) {
        return null;
    }


    private DirectoryResponseDTO getDirectoryInfo(String path) {

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(BUCKET_NAME)
                        .prefix(path)
                        .recursive(false)
                        .maxKeys(1)
                        .build()
        );

        if (!results.iterator().hasNext()) {
            throw new ResourceNotFoundException(
                    DIRECTORY_NOT_FOUND_MESSAGE
            );
        }

        return DirectoryResponseDTO.builder()
                .path(path)
                .type(DIRECTORY)
                .build();
    }


    private FileResponseDTO getFileInfo(String path) {

        try {
            StatObjectResponse response = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(path)
                            .build()
            );


            return FileResponseDTO.builder()
                    .path(path)
                    .name(path)
                    .size(response.size())
                    .type(FILE)
                    .build();

        } catch (ErrorResponseException e) {

            if ("NoSuchKey".equals(e.errorResponse().code())) {
                throw new ResourceNotFoundException(FILE_NOT_FOUND_MESSAGE);
            }

            throw new StorageException("Failed to get file information");

        } catch (Exception e) {
            throw new StorageException("Failed to get file information");
        }
    }

    @Override
    public boolean bucketExist(String bucketName) {
        try {
           return minioClient.bucketExists(
                    BucketExistsArgs
                            .builder()
                            .bucket(bucketName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
