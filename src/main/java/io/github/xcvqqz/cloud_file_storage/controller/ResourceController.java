package io.github.xcvqqz.cloud_file_storage.controller;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequestDTO;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponseDTO;
import io.github.xcvqqz.cloud_file_storage.service.file.ResourceService;
import io.github.xcvqqz.cloud_file_storage.service.file.ResourceServiceImpl;
import io.github.xcvqqz.cloud_file_storage.service.storage.MinioService;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resource")
public class ResourceController {

    private final ResourceServiceImpl resourceService;

//    @GetMapping("/bucket/{bucketName}")
//    public boolean bucketExist(@PathVariable String bucketName) {
//        return minioService.bucketExist(bucketName);
//    }


    @GetMapping
    public ResponseEntity<ResourceResponseDTO> getResourceInfo(@ModelAttribute ResourceRequestDTO request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(resourceService.getResourceInfo(request));
    }

//
//    @DeleteMapping
//    public ResponseEntity<Void> deleteResource(){
//        return ResponseEntity.noContent(minioService.)
//    }



}