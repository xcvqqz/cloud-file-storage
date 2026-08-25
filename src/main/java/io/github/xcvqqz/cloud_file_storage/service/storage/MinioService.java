package io.github.xcvqqz.cloud_file_storage.service.storage;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequestDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.DirectoryResponseDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.FileResponseDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponseDTO;
import io.github.xcvqqz.cloud_file_storage.entity.ResourceType;
import io.github.xcvqqz.cloud_file_storage.exception.DirectoryNotFoundException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static io.github.xcvqqz.cloud_file_storage.entity.ResourceType.FILE;


@RequiredArgsConstructor
@Service
public class MinioService implements FileStorageService {

    private static final String BUCKET_NAME = "my-files";
    private static final String DIRECTORY_TYPE = "DIRECTORY";
    private static final String FILE_TYPE = "FILE";

    private static final String DIRECTORY_NOT_FOUND_MESSAGE =
            "The folder at the specified path was not found." +
            " Please check that the folder exists or that the path was entered correctly";

    private static final String FILE_NOT_FOUND_MESSAGE = "The file at the specified path was not found. " +
            "Please check that the file exists or that the path was entered correctly";

    private final MinioClient minioClient;

    @Override
    public ResourceResponseDTO getResourceInfo(ResourceRequestDTO request)
            throws ServerException,
            InsufficientDataException,
            ErrorResponseException,
            IOException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            InvalidResponseException,
            XmlParserException,
            InternalException{

        String path = request.path();
        return path.endsWith("/") ? getDirectoryInfo(path) : getFileInfo(path);
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
            throw new DirectoryNotFoundException(
                    DIRECTORY_NOT_FOUND_MESSAGE
            );
        }

        return DirectoryResponseDTO.builder()
                .path(path)
                .type(ResourceType.DIRECTORY)
                .build();

    }


    private FileResponseDTO getFileInfo(String path)
            throws ServerException,
            InsufficientDataException,
            ErrorResponseException,
            IOException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            InvalidResponseException,
            XmlParserException,
            InternalException {

        try {
            StatObjectResponse response = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(path)
                            .build()
            );

            String fileName = Paths.get(path)
                    .getFileName()
                    .toString();

            return FileResponseDTO.builder()
                    .path(response.object())
                    .name(fileName)
                    .size(response.size())
                    .type(FILE)
                    .build();

        } catch (ErrorResponseException e) {

            if ("NoSuchKey".equals(e.errorResponse().code())) {
                throw new FileNotFoundException(
                        FILE_NOT_FOUND_MESSAGE
                );
            }

            throw e;
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
