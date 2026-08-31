package io.github.xcvqqz.cloud_file_storage.service.storage;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequestDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.DirectoryResponseDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.FileResponseDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponseDTO;
import io.github.xcvqqz.cloud_file_storage.exception.DirectoryNotFoundException;
import io.github.xcvqqz.cloud_file_storage.service.auth.UserService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
    private final UserService userService;

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
            InternalException {

        Long currentUserId = userService.getCurrentUserId();

        String requestPath = request.path();
        String objectPath = buildPath(requestPath, currentUserId);

        return requestPath.endsWith("/") ?
                getDirectoryInfo(requestPath, objectPath) :
                getFileInfo(requestPath, objectPath);
    }

    @Override
    public ResourceResponseDTO deleteResource(ResourceRequestDTO request) {
        return null;
    }


    private DirectoryResponseDTO getDirectoryInfo(String responsePath, String objectPath) {

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(BUCKET_NAME)
                        .prefix(objectPath)
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
                .path(responsePath)
                .type(DIRECTORY)
                .build();
    }


    private FileResponseDTO getFileInfo(String responsePath, String objectPath)
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
                            .object(objectPath)
                            .build()
            );

            String fileName = Paths.get(objectPath)
                    .getFileName()
                    .toString();

            return FileResponseDTO.builder()
                    .path(responsePath)
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



    private String buildPath(String path, Long userId) {
        return String.format("user-%d-files/%s", userId, path);
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