package io.github.xcvqqz.cloud_file_storage.service.storage;


import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileStorageService {

    InputStream upload(MultipartFile multipartFile);

    boolean bucketExist(String bucketName);

}
