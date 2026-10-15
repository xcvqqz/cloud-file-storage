package io.github.xcvqqz.cloud_file_storage.controller;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.service.directory.CreateDirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directory")
public class DirectoryController {

    private final CreateDirectoryService createDirectoryService;

    @PostMapping
    public ResponseEntity<ResourceResponse> createDirectory(@ModelAttribute ResourceRequest path){
        return ResponseEntity.status(HttpStatus.CREATED).body(createDirectoryService.create(path));
    }

}