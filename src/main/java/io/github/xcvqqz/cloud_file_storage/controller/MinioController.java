package io.github.xcvqqz.cloud_file_storage.controller;


import io.github.xcvqqz.cloud_file_storage.service.storage.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/minio")
public class MinioController {

    private final MinioService minioService;

    @GetMapping("/bucket/{bucketName}")
    public boolean bucketExist(@PathVariable String bucketName) {
        return minioService.bucketExist(bucketName);
    }
}