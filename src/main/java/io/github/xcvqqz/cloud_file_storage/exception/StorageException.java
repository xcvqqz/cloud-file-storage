package io.github.xcvqqz.cloud_file_storage.exception;

public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }
    public StorageException(String message, Throwable e){super(message, e);}
}
