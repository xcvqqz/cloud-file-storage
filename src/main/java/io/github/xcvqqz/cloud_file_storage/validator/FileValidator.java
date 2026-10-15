package io.github.xcvqqz.cloud_file_storage.validator;


import io.github.xcvqqz.cloud_file_storage.exception.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileValidator {

    private static final String FILE_EMPTY_MESSAGE = "File must not be empty";
    private static final String FILE_NULL_MESSAGE = "File must not be null";
    private static final String FILE_NAME_NULL_MESSAGE = "File name must not be null";

    public
    void validate(MultipartFile file) {
        if (file == null) {
            throw new BadRequestException(FILE_NULL_MESSAGE);
        }
        if (file.isEmpty()) {
            throw new BadRequestException(FILE_EMPTY_MESSAGE);
        }
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            throw new BadRequestException(FILE_NAME_NULL_MESSAGE);
        }
    }

}
