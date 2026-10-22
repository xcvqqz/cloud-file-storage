package io.github.xcvqqz.cloud_file_storage.resolver;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.entity.ResourceType;
import io.github.xcvqqz.cloud_file_storage.exception.BadRequestException;
import io.github.xcvqqz.cloud_file_storage.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ResourcePathResolver {

    private final UserService userService;

    public String resolve(ResourceRequest request){

       Long userId = userService.getCurrentUserId();

       return String.format(
                "user-%d-files/%s",
                userId,
                request.path());
    }


    public String resolve(ResourceRequest request, MultipartFile multipartFile){

        Long userId = userService.getCurrentUserId();

        String folderPath = request.path().endsWith("/") ?
                            request.path()  :
                            request.path() + "/";

        return String.format(
                "user-%d-files/%s/%s",
                userId,
                folderPath,
                multipartFile.getOriginalFilename());
    }



}
