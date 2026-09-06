package io.github.xcvqqz.cloud_file_storage.controller;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.service.file.ResourceServiceImpl;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<ResourceResponse> getResourceInfo(@ModelAttribute ResourceRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(resourceService.getResourceInfo(request));
    }

    @PostMapping
    public ResponseEntity<ResourceResponse> upload(@ModelAttribute ResourceRequest request,
                                                   @RequestParam("file") MultipartFile file){
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceService.upload(request, file));
    }

//
//    @DeleteMapping
//    public ResponseEntity<Void> deleteResource(){
//        return ResponseEntity.noContent(minioService.)
//    }



}