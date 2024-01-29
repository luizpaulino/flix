package com.flix.video.controller;

import com.flix.video.dto.request.VideoRequestDTO;
import com.flix.video.dto.response.VideoResponseDTO;
import com.flix.video.service.VideoService;
import com.flix.video.service.VideoUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VideoControllerTest {

    @Mock
    private VideoService videoService;

    @Mock
    private VideoUploadService videoUploadService;

    @InjectMocks
    private VideoController videoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchVideos_shouldReturnPageOfVideoResponseDTO() {
        // Arrange
        when(videoService.searchVideos(anyString(), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(new VideoResponseDTO())));

        // Act
        Page<VideoResponseDTO> result = videoController.searchVideos("field", "query", "ASC", Pageable.unpaged());

        // Assert
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void handleFileUpload_shouldUploadVideoSuccessfully() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "test-video.mp4", "video/mp4", "test video content".getBytes());

        // Act
        ResponseEntity<Void> responseEntity = videoController.handleFileUpload(file, "videoId");

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(videoUploadService).uploadVideo(eq(file), eq("videoId"));
    }

    @Test
    void updateVideo_shouldReturnVideoResponseDTO() {
        // Arrange
        VideoRequestDTO videoRequestDTO = new VideoRequestDTO();
        when(videoService.updateVideo(eq(videoRequestDTO), anyString())).thenReturn(new VideoResponseDTO());

        // Act
        ResponseEntity<VideoResponseDTO> responseEntity = videoController.updateVideo(videoRequestDTO, "videoId");

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(VideoResponseDTO.class, responseEntity.getBody().getClass());
    }

    @Test
    void removeVideo_shouldRemoveVideoSuccessfully() {
        // Act
        ResponseEntity<Void> responseEntity = videoController.removeVideo("videoId");

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(videoService).removeVideo(eq("videoId"));
    }

    @Test
    void addVideo_shouldReturnVideoResponseDTO() {
        // Arrange
        VideoRequestDTO videoRequestDTO = new VideoRequestDTO();
        when(videoService.addVideo(eq(videoRequestDTO))).thenReturn(new VideoResponseDTO());

        // Act
        ResponseEntity<VideoResponseDTO> responseEntity = videoController.addVideo(videoRequestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(VideoResponseDTO.class, responseEntity.getBody().getClass());
    }
}
