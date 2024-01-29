package com.flix.shared.exception.controller;

import com.flix.shared.exception.models.VideoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleException_shouldReturnInternalServerError() {
        Exception exception = new Exception("Internal Server Error");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void handleVideoException_shouldReturnUnprocessableEntityWithErrorMessage() {
        VideoException videoException = new VideoException("Video Exception");

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleVideoException(videoException);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertEquals("{\"message\":\"Video Exception\"}", responseEntity.getBody());
    }

    @Test
    void argumentNotValid_shouldReturnBadRequestWithInvalidFields() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);

        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(new FieldError("objectName", "fieldName", "error message")));

        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        ResponseEntity<Object> responseEntity = globalExceptionHandler.argumentNotValid(exception);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }
}
