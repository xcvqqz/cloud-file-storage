package io.github.xcvqqz.cloud_file_storage.service.storage;


import io.github.xcvqqz.cloud_file_storage.service.file.FileService;
import io.github.xcvqqz.cloud_file_storage.service.file.FileServiceImpl;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@RequiredArgsConstructor
@Service
public class MinioService implements FileStorageService {

    private final MinioClient minioClient;

    @Override
    public InputStream upload(MultipartFile multipartFile) {
        return null;
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
