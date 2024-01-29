package com.flix.shared.exception.models;

public class VideoException extends RuntimeException {

    public VideoException(String message) {
        super(message);
    }

    public VideoException(String message, Throwable cause) {
        super(message, cause);
    }
}
