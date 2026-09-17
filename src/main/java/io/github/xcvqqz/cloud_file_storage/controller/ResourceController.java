package io.github.xcvqqz.cloud_file_storage.controller;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.service.file.ResourceServiceImpl;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
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


    //нужно сделать проверки на 2 слеша, несуществующий путь, пустой путь, скачивание файла, скачивание папки

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@ModelAttribute ResourceRequest request) {

        Resource resource = resourceService.download(request);

        String fileName = Paths.get(request.path())
                .getFileName()
                .toString();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(httpHeaders ->
                        httpHeaders.setContentDisposition(
                                ContentDisposition.attachment()
                                        .filename(fileName)
                                        .build()))
                .body(resource);
    }



//
//    @DeleteMapping
//    public ResponseEntity<Void> deleteResource(){
//        return ResponseEntity.noContent(minioService.)
//    }



}