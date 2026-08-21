package io.github.xcvqqz.cloud_file_storage.service.storage;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequestDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.ResourceResponseDTO;
import io.github.xcvqqz.cloud_file_storage.entity.ResourceType;
import io.github.xcvqqz.cloud_file_storage.service.file.FileService;
import io.github.xcvqqz.cloud_file_storage.service.file.FileServiceImpl;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static io.github.xcvqqz.cloud_file_storage.entity.ResourceType.FILE;


@RequiredArgsConstructor
@Service
public class MinioService implements FileStorageService {

    private final MinioClient minioClient;

//    String path,
//    String name,
//    byte size,
//    String type


    @Override
    public ResourceResponseDTO getResourceInfo(ResourceRequestDTO request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        String path = request.path();

        if(path.endsWith("/")){
            return getDirectoryInfo(path);
        } else {
            return getFileInfo(path);
        }
    }


    private ResourceResponseDTO getDirectoryInfo(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        StatObjectResponse response = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket("my-files")
                        .object(path)
                        .build()
        );

        return new ResourceResponseDTO(
                response.object(),
                "-",
                0,
                "DIRECTORY");
    }


    private ResourceResponseDTO getFileInfo(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        StatObjectResponse response = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket("my-files")
                        .object(path)
                        .build()
        );

        String fileName = Paths.get(path).getFileName().toString();

        return new ResourceResponseDTO(
                response.object(),
                fileName,
                response.size(),  //пока поменял на размер на Long
                "FILE");
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
