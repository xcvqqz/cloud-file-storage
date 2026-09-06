package io.github.xcvqqz.cloud_file_storage.resolver;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
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
                normalize(request.path()));
    }

    public String resolve(ResourceRequest request, MultipartFile multipartFile){
       return resolve(request) + multipartFile.getOriginalFilename();
    }


    private String normalize(String path) {
        if (path == null || path.isBlank()) {
            return "";
        }
        return path.endsWith("/")
                ? path
                : path + "/";
    }

}
