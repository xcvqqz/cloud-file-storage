package io.github.xcvqqz.cloud_file_storage.controller;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.resource.ResourceResponse;
import io.github.xcvqqz.cloud_file_storage.service.resource.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resource")
public class ResourceController {

    private final GetResourceInfoService getResourceInfoService;
    private final DownloadResourceService downloadResourceService;
    private final MoveOrRenameResourceService moveOrRenameResourceService;
    private final UploadResourceService uploadResourceService;



    @GetMapping
    public ResponseEntity<ResourceResponse> getResourceInfo(@ModelAttribute ResourceRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(getResourceInfoService.getResourceInfo(request));
    }

    @PostMapping
    public ResponseEntity<ResourceResponse> upload(@ModelAttribute ResourceRequest request,
                                                   @RequestParam("file") MultipartFile file){
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadResourceService.upload(request, file));
    }


    //нужно сделать проверки на 2 слеша, несуществующий путь, пустой путь, скачивание файла, скачивание папки

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@ModelAttribute ResourceRequest request) {

        Resource resource = downloadResourceService.download(request);

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


    @PostMapping("/move")
    public ResponseEntity<ResourceResponse> move(@ModelAttribute("from") ResourceRequest from,
                                  @ModelAttribute("to") ResourceRequest to) {
        return ResponseEntity.status(HttpStatus.OK).body(moveOrRenameResourceService.moveOrRename(from, to));
    }



//
//    @DeleteMapping
//    public ResponseEntity<Void> deleteResource(){
//        return ResponseEntity.noContent(minioService.)
//    }



}