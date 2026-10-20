package io.github.xcvqqz.cloud_file_storage.controller;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.DirectoryResponse;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.service.directory.CreateDirectoryService;
import io.github.xcvqqz.cloud_file_storage.service.directory.GetDirectoryContentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directory")
public class DirectoryController {

    private final CreateDirectoryService createDirectoryService;
    private final GetDirectoryContentsService getDirectoryContentsService;

    @PostMapping
    public ResponseEntity<DirectoryResponse> createDirectory(@ModelAttribute ResourceRequest path){
        return ResponseEntity.status(HttpStatus.CREATED).body(createDirectoryService.create(path));
    }

    @GetMapping
    public ResponseEntity<List<ResourceResponse>> getDirectoryContents(@ModelAttribute ResourceRequest path){
        return ResponseEntity.status(HttpStatus.OK).body(getDirectoryContentsService.getContentsInfo(path));
    }

}