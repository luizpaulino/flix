package com.flix.video.service;

import com.flix.aggregator.service.AggregateService;
import com.flix.video.persistence.entity.VideoDocument;
import com.flix.video.persistence.entity.VideoSearchDocument;
import com.flix.video.persistence.repository.VideoDocumentRepository;
import com.flix.video.persistence.repository.VideoSearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class VideoUploadServiceTest {

    @Mock
    private VideoDocumentRepository videoDocumentRepository;

    @Mock
    private VideoSearchRepository videoSearchRepository;

    @Mock
    private VideoService videoService;

    @Mock
    private AggregateService aggregateService;

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private VideoUploadService videoUploadService;

    @BeforeEach
    void setUp() {
        // Inicialização do Mockito
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void uploadVideo_shouldThrowIOExceptionOnUploadFailure() {
        // Arrange
        String idVideo = "123";
        String originalFileName = "test.mp4";
        MultipartFile videoFile = new MockMultipartFile("file", originalFileName, "video/mp4", "test data".getBytes());
        VideoDocument videoDocument = createMockVideoDocument(idVideo);

        // Configurar o comportamento do mock para retornar o vídeo quando findVideo é chamado
        when(videoService.findVideo(eq(idVideo))).thenReturn(videoDocument);

        // Certificar-se de que o método findVideo é chamado
        assertThrows(IOException.class, () -> videoUploadService.uploadVideo(videoFile, idVideo));

        // Verificar as interações com os mocks
        verify(videoDocumentRepository, never()).save(any(VideoDocument.class));
        verify(videoSearchRepository, never()).save(any(VideoSearchDocument.class));
        verify(aggregateService, never()).aggregateTotalVideo();
    }


    @Test
    void generateUniqueFileName_shouldGenerateUniqueFileName() {
        // Arrange
        String originalFileName = "test.mp4";
        String idVideo = "123";
        String expectedFileName = idVideo + ".mp4";

        // Act
        String result = videoUploadService.generateUniqueFileName(originalFileName, idVideo);

        // Assert
        assertEquals(expectedFileName, result);
    }

    private VideoDocument createMockVideoDocument(String idVideo) {
        VideoDocument videoDocument = new VideoDocument();
        videoDocument.setId(idVideo);
        // Set other properties
        return videoDocument;
    }

    private VideoSearchDocument createMockVideoSearchDocument(String idVideo) {
        VideoSearchDocument videoSearchDocument = new VideoSearchDocument();
        videoSearchDocument.setId(idVideo);
        // Set other properties
        return videoSearchDocument;
    }
}
