package io.github.xcvqqz.cloud_file_storage.service.storage;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.DirectoryResponse;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.FileResponse;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.exception.ResourceNotFoundException;
import io.github.xcvqqz.cloud_file_storage.exception.StorageException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    public ResourceResponse getResourceInfo(String path) {
//        int index = path.lastIndexOf('/');
//        String result = path.substring(index + 1);

        return path.endsWith("/") ?
                getDirectoryInfo(path) :
                getFileInfo(path);
    }





    @Override
    public void upload(String resourcePath, MultipartFile file) {

        try {
           minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(BUCKET_NAME)
                    .object(resourcePath)
                    .stream(
                            file.getInputStream(),
                            file.getSize(),
                            -1
                    )
                    .contentType(file.getContentType())
                    .build());
        } catch (Exception e){
            throw new StorageException("Failed to upload file: " + file.getOriginalFilename(), e);
        }
    }



    @Override
    public Resource downloadFile(String path){
            try{
                InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(BUCKET_NAME)
                                .object(path)
                                .build()
                );
                return new InputStreamResource(inputStream);
            } catch (ErrorResponseException e) {
                if ("NoSuchKey".equals(e.errorResponse().code())) {
                    throw new ResourceNotFoundException("Resource not found: " + path);
                }
                throw new StorageException("Failed to download resource: " + path, e);

            } catch (Exception e){
                throw new StorageException("Failed to download resource: " + path, e);
            }
    }

    @Override
    public Resource downloadDirectory(String path) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

       try {

           Iterable<Result<Item>> results = minioClient.listObjects(
                   ListObjectsArgs.builder()
                           .bucket(BUCKET_NAME)
                           .prefix(path)
                           .recursive(true)
                           .build()
           );

           if (!results.iterator().hasNext()) {
               throw new ResourceNotFoundException(
                       DIRECTORY_NOT_FOUND_MESSAGE         //нужно изменить!!!!!!!!!!!!!!!!!!!!
               );
           }

           try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

               for (Result<Item> result : results) {
                   Item item = result.get();
                   String objectName = item.objectName();

                   String relativePath = objectName.substring(path.length());

                   ZipEntry zipEntry = new ZipEntry(relativePath);

                   zipOutputStream.putNextEntry(zipEntry);

                   try (InputStream inputStream = downloadFile(objectName).getInputStream()) {
                       inputStream.transferTo(zipOutputStream);
                   }
                   zipOutputStream.closeEntry();
               }
           }
       } catch (ResourceNotFoundException e){
           throw e;
       } catch (Exception e){
           throw new StorageException("Failed to download resource: " + path, e);
       }
        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }


    private DirectoryResponse getDirectoryInfo(String path) {

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

        return DirectoryResponse.builder()
                .path(path)
                .type(DIRECTORY)
                .build();
    }


    private FileResponse getFileInfo(String path) {

        try {
            StatObjectResponse response = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(path)
                            .build()
            );


            return FileResponse.builder()
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
            throw new StorageException("Failed to get file information", e);
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
