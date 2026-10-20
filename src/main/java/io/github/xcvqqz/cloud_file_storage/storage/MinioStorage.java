package io.github.xcvqqz.cloud_file_storage.storage;


import io.github.xcvqqz.cloud_file_storage.dto.response.resource.DirectoryResponse;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.FileResponse;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.entity.ResourceType;
import io.github.xcvqqz.cloud_file_storage.exception.ResourceNotFoundException;
import io.github.xcvqqz.cloud_file_storage.exception.StorageException;
import io.github.xcvqqz.cloud_file_storage.mapper.ResourceMapper;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static io.github.xcvqqz.cloud_file_storage.entity.ResourceType.DIRECTORY;
import static io.github.xcvqqz.cloud_file_storage.entity.ResourceType.FILE;

@Slf4j
@RequiredArgsConstructor
@Component
public class MinioStorage implements ObjectStorage {

    private static final String BUCKET_NAME = "user-files";

    private static final String DIRECTORY_NOT_FOUND_MESSAGE =
            "The folder at the specified path was not found." +
                    " Please check that the folder exists or that the path was entered correctly";

    private static final String FILE_NOT_FOUND_MESSAGE = "The file at the specified path was not found. " +
            "Please check that the file exists or that the path was entered correctly";


    private final ResourceMapper resourceMapper;
    private final MinioClient minioClient;


    @Override
    public ResourceResponse getResourceInfo(String path) {

        return path.endsWith("/") ?
                getDirectoryInfo(path) :
                getFileInfo(path);
    }


    public  FileResponse moveFile(String from, String to) {

        StatObjectResponse response;

        try {
             minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(to)
                            .source(
                                    CopySource.builder()
                                            .bucket(BUCKET_NAME)
                                            .object(from)
                                            .build()
                            )
                            .build()
            );

            response = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(to)
                            .build()
            );

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(from)
                            .build()
            );

        } catch (Exception e){
            throw new StorageException("произошла ошибка при изменение имени файла", e);
        }
        return resourceMapper.toFileResponse(to, response);
    }

    public DirectoryResponse moveDirectory(String from, String to)  {

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(BUCKET_NAME)
                            .prefix(from)
                            .recursive(true)
                            .build()
            );

            for (Result<Item> result : results) {

                Item item = result.get();
                String sourceObject = item.objectName();
                String relativePath =
                        sourceObject.substring(from.length());

                String targetObject =
                        to + relativePath;

                minioClient.copyObject(
                        CopyObjectArgs.builder()
                                .bucket(BUCKET_NAME)
                                .object(targetObject)
                                .source(
                                        CopySource.builder()
                                                .bucket(BUCKET_NAME)
                                                .object(sourceObject)
                                                .build()
                                )
                                .build()
                );
            }
            

            results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(BUCKET_NAME)
                            .prefix(from)
                            .recursive(true)
                            .build()
            );

            for (Result<Item> result : results) {

                String objectName = result.get().objectName();

                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(BUCKET_NAME)
                                .object(objectName)
                                .build()
                );
            }

        } catch (Exception e) {

            throw new StorageException(
                    "Failed to move directory from " + from + " to " + to,
                    e
            );
        }

        return resourceMapper.toDirectoryResponse(to);
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

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(BUCKET_NAME)
                            .prefix(path)
                            .recursive(false)
                            .maxKeys(1)
                            .build()
            );

            Iterator<Result<Item>> iterator = results.iterator();

            if (!iterator.hasNext()) {
                throw new ResourceNotFoundException(
                        DIRECTORY_NOT_FOUND_MESSAGE
                );
            }
            iterator.next().get();

            return resourceMapper.toDirectoryResponse(path.substring(path.indexOf("/")));

        } catch (ResourceNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new StorageException("Failed to get directory info: ", e);
        }

    }

    public List<ResourceResponse> getContentByDirectory(String path) {

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(BUCKET_NAME)
                        .prefix(path)
                        .recursive(false)
                        .build()
        );

      List<ResourceResponse> resources = new ArrayList<>();

         for(Result<Item> result : results) {

      }

    }


    private FileResponse getFileInfo(String path) {

        try {
            StatObjectResponse response = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(path)
                            .build()
            );

            return resourceMapper.toFileResponse(path, response);

        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                throw new ResourceNotFoundException(FILE_NOT_FOUND_MESSAGE);
            }
            throw new StorageException("Failed to get file information");

        } catch (Exception e) {
            throw new StorageException("Failed to get file information", e);
        }
    }

    public boolean resourceExists(String resourcePath){
        if(resourcePath.endsWith("/")){
            return directoryExists(resourcePath);
        } else {
            return fileExists(resourcePath);
        }
    }

    private boolean fileExists(String filePath){
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(filePath)
                            .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                return false;
            }
            throw new StorageException("An error occurred while retrieving information about the availability (boolean) of the resource");
        } catch (Exception e) {
            throw new StorageException("Failed to get file exist information", e);
        }
    }

    public DirectoryResponse createDirectory(String path) {

        try {
          minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(path)
                            .stream(
                                    new ByteArrayInputStream(new byte[0]),
                                    0,
                                    -1
                            )
                            .build()
            );
        } catch (Exception e) {
            throw new StorageException(
                    "Failed to create directory: " + path,
                    e
            );
        }
        return DirectoryResponse.builder()
                .path(path.substring(path.indexOf("/")+1))
                .type(DIRECTORY)
                .build();
    }

    public List<ResourceResponse> searchResources(String query, String userRoot)  {


        //не отображаются папки!!!!!!!!!!!!!!!!

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(BUCKET_NAME)
                            .prefix(userRoot)
                            .recursive(true)
                            .build()
            );

            List<ResourceResponse> foundResources = new ArrayList<>();

            for (Result<Item> result : results) {

                Item item = result.get();
                Path pathItem = Path.of(item.objectName());

                String parentPath = pathItem.getParent().toString() != null ? pathItem.getParent().toString() + "/" : "";

                String fileName = pathItem.getFileName().toString() != null ? pathItem.getFileName().toString() : "";

                Long size = !(item.size() == 0) ? item.size() : null;

                boolean isDirectory = item.objectName().endsWith("/");

                if (fileName.toLowerCase().contains(query.toLowerCase())) {
                    if (isDirectory) {
                        foundResources.add(DirectoryResponse.builder()
                                        .path(pathItem.toString())
                                        .type(DIRECTORY)
                                        .build());
                    } else {
                        foundResources.add(FileResponse.builder()
                                .path(parentPath)
                                .name(fileName)
                                .size(size)
                                .type(FILE)
                                .build());
                    }
                }
            }

            if(foundResources.isEmpty()){
                throw new ResourceNotFoundException("No resources were found for your request, please try searching for your resource using a different keyword");
            }
            return foundResources;
        } catch (Exception e) {
            throw new StorageException("An error occurred while retrieving the list of resources for the request - '" + query + "' , please try again", e);
        }
    }

    private boolean directoryExists(String directoryPath){

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(BUCKET_NAME)
                            .prefix(directoryPath)
                            .recursive(false)
                            .maxKeys(1)
                            .build()
            );
            return results.iterator().hasNext();
        } catch (Exception e) {
            log.error("An error occurred while checking the uniqueness of the directory");
            throw new StorageException("An error occurred while working with a file or directory" + directoryPath, e);
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