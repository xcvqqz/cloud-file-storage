package io.github.xcvqqz.cloud_file_storage.validator;


import io.github.xcvqqz.cloud_file_storage.dto.request.ResourceRequest;
import io.github.xcvqqz.cloud_file_storage.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ResourcePathValidator {

    private static final int MAX_PATH_LENGTH = 1024;
    private static final Pattern INVALID_PATH_PATTERN = Pattern.compile(
            "[\\\\\\x00-\\x1F]"
    );

    private static final String NOT_NULL_MESSAGE = "The path cannot be empty or null";
    private static final String LONG_PATH_LENGTH_MESSAGE = "Path is too long";
    private static final String DOUBLE_SLASHES_MESSAGE = "The path cannot contain double slashes //";
    private static final String STARTS_SLASHES_MESSAGE = "Path must not start with '/'";
    private static final String INVALID_PATH_MESSAGE = "Path contains invalid characters";
    private static final String INVALID_SEGMENT_MESSAGE = "Path must not contain '.' or '..' segments";

    public void validate(ResourceRequest request){

        String requestPath = request.path();

        validateNotNull(requestPath);
        validatePathLength(requestPath);
        validateDoubleSlashes(requestPath);
        validateStartsSlashes(requestPath);
        validateSegments(requestPath);
        validateInvalidCharacters(requestPath);
    }

    private void validateNotNull(String path) {
        if (path == null || path.isBlank()) {
            throw new BadRequestException(NOT_NULL_MESSAGE);
        }
    }

    private void validatePathLength(String path) {
        if (path.length() > MAX_PATH_LENGTH) {
            throw new BadRequestException(LONG_PATH_LENGTH_MESSAGE);
        }
    }

    private void validateDoubleSlashes(String path) {
        if(path.contains("//")){
            throw new BadRequestException(DOUBLE_SLASHES_MESSAGE);
        }
    }

    private void validateStartsSlashes(String path) {
        if (path.startsWith("/")) {
            throw new BadRequestException(STARTS_SLASHES_MESSAGE);
        }
    }

    private void validateInvalidCharacters(String path) {
        if (INVALID_PATH_PATTERN.matcher(path).find()) {
            throw new BadRequestException(INVALID_PATH_MESSAGE);
        }
    }

    private void validateSegments(String path) {

        String normalizedPath = path.endsWith("/")
                ? path.substring(0, path.length() - 1)
                : path;

        for (String segment : normalizedPath.split("/")) {
            if (segment.equals(".") || segment.equals("..")) {
                throw new BadRequestException(INVALID_SEGMENT_MESSAGE);
            }
        }
    }
}