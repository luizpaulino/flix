package com.flix.video.service;

import com.flix.shared.exception.models.VideoException;
import com.flix.video.dto.request.VideoRequestDTO;
import com.flix.video.dto.response.VideoResponseDTO;
import com.flix.video.persistence.entity.VideoDocument;
import com.flix.video.persistence.entity.VideoSearchDocument;
import com.flix.video.persistence.repository.VideoDocumentRepository;
import com.flix.video.persistence.repository.VideoSearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VideoServiceTest {

    @Mock
    private VideoSearchRepository videoSearchRepository;

    @Mock
    private VideoDocumentRepository videoDocumentRepository;

    @InjectMocks
    private VideoService videoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchVideos_shouldReturnAllVideosWhenQueryAndFieldNotProvided() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "publicationDate");
        when(videoSearchRepository.findAll(pageable)).thenReturn(createMockVideoSearchPage());

        // Act
        Page<VideoResponseDTO> result = videoService.searchVideos(null, null, "DESC", pageable);

        // Assert
        assertFalse(result.isEmpty());
        verify(videoSearchRepository, times(1)).findAll(pageable);
    }

    @Test
    void searchVideos_shouldReturnVideosByTitle() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "publicationDate");
        when(videoSearchRepository.findByTitle("Test", pageable)).thenReturn(createMockVideoSearchPage());

        // Act
        Page<VideoResponseDTO> result = videoService.searchVideos("title", "Test", "DESC", pageable);

        // Assert
        assertFalse(result.isEmpty());
        verify(videoSearchRepository, times(1)).findByTitle("Test", pageable);
    }

    // Add more test cases for other search scenarios

    @Test
    void addVideo_shouldReturnVideoResponseDTO() {
        // Arrange
        VideoRequestDTO requestDTO = createMockVideoRequestDTO();
        VideoDocument videoDocument = createMockVideoDocument();
        when(videoDocumentRepository.save(any())).thenReturn(videoDocument);

        // Act
        VideoResponseDTO result = videoService.addVideo(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(videoDocumentRepository, times(1)).save(any());
    }

    @Test
    void updateVideo_shouldReturnVideoResponseDTO() {
        // Arrange
        String idVideo = "123";
        VideoRequestDTO requestDTO = createMockVideoRequestDTO();
        VideoDocument videoDocument = createMockVideoDocument();
        when(videoDocumentRepository.findById(idVideo)).thenReturn(Optional.of(videoDocument));
        when(videoDocumentRepository.save(any())).thenReturn(videoDocument);
        when(videoSearchRepository.findByTitle(anyString(), any())).thenReturn(createMockVideoSearchPage());

        // Act
        VideoResponseDTO result = videoService.updateVideo(requestDTO, idVideo);

        // Assert
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(videoDocumentRepository, times(1)).save(any());
        verify(videoSearchRepository, times(1)).deleteById(eq(idVideo));
        verify(videoSearchRepository, times(1)).save(any());
    }

    @Test
    void updateVideo_shouldThrowVideoExceptionWhenVideoNotFound() {
        // Arrange
        String idVideo = "123";
        VideoRequestDTO requestDTO = createMockVideoRequestDTO();
        when(videoDocumentRepository.findById(idVideo)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(VideoException.class, () -> videoService.updateVideo(requestDTO, idVideo));
        verify(videoDocumentRepository, never()).save(any());
        verify(videoSearchRepository, never()).deleteById(anyString());
        verify(videoSearchRepository, never()).save(any());
    }

    @Test
    void searchVideos_shouldReturnVideosByCategory() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "publicationDate");
        when(videoSearchRepository.findByCategory("Test", pageable)).thenReturn(createMockVideoSearchPage());

        // Act
        Page<VideoResponseDTO> result = videoService.searchVideos("category", "Test", "DESC", pageable);

        // Assert
        assertFalse(result.isEmpty());
        verify(videoSearchRepository, times(1)).findByCategory("Test", pageable);
    }

    @Test
    void searchVideos_shouldReturnVideosByPublicationDate() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "publicationDate");
        when(videoSearchRepository.findByPublicationDate(any(LocalDate.class), eq(pageable))).thenReturn(createMockVideoSearchPage());

        // Act
        Page<VideoResponseDTO> result = videoService.searchVideos("publication_date", "01/01/2022", "DESC", pageable);

        // Assert
        assertFalse(result.isEmpty());
        verify(videoSearchRepository, times(1)).findByPublicationDate(any(LocalDate.class), eq(pageable));
    }

    @Test
    void searchVideos_shouldReturnEmptyPageWhenInvalidField() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "publicationDate");

        // Act
        Page<VideoResponseDTO> result = videoService.searchVideos("invalid_field", "Test", "DESC", pageable);

        // Assert
        assertTrue(result.isEmpty());
        verify(videoSearchRepository, never()).findByTitle(anyString(), any());
        verify(videoSearchRepository, never()).findByCategory(anyString(), any());
        verify(videoSearchRepository, never()).findByPublicationDate(any(LocalDate.class), any());
    }

    @Test
    void removeVideo_shouldRemoveVideo() {
        // Arrange
        String idVideo = "123";
        VideoDocument videoDocument = createMockVideoDocument();
        when(videoDocumentRepository.findById(idVideo)).thenReturn(Optional.of(videoDocument));
        when(videoSearchRepository.findByTitle(anyString(), any())).thenReturn(createMockVideoSearchPage());

        // Act
        videoService.removeVideo(idVideo);

        // Assert
        verify(videoSearchRepository, times(1)).deleteById(eq(idVideo));
        verify(videoDocumentRepository, times(1)).delete(eq(videoDocument));
    }

    @Test
    void removeVideo_shouldThrowVideoExceptionWhenVideoNotFound() {
        // Arrange
        String idVideo = "123";
        when(videoDocumentRepository.findById(idVideo)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(VideoException.class, () -> videoService.removeVideo(idVideo));
        verify(videoSearchRepository, never()).deleteById(anyString());
        verify(videoDocumentRepository, never()).delete(any());
    }

    @Test
    void findVideo_shouldReturnVideo() {
        // Arrange
        String idVideo = "123";
        VideoDocument videoDocument = createMockVideoDocument();
        when(videoDocumentRepository.findById(idVideo)).thenReturn(Optional.of(videoDocument));

        // Act
        VideoDocument result = videoService.findVideo(idVideo);

        // Assert
        assertNotNull(result);
        assertEquals(videoDocument, result);
        verify(videoDocumentRepository, times(1)).findById(eq(idVideo));
    }

    @Test
    void findVideo_shouldThrowVideoExceptionWhenVideoNotFound() {
        // Arrange
        String idVideo = "123";
        when(videoDocumentRepository.findById(idVideo)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(VideoException.class, () -> videoService.findVideo(idVideo));
        verify(videoDocumentRepository, times(1)).findById(eq(idVideo));
    }

    @Test
    void findVideoRecommendations_shouldReturnVideoSearchDocuments() {
        // Arrange
        Set<String> categories = Set.of("Category1", "Category2");
        PageRequest pageable = PageRequest.of(0, 10);
        when(videoSearchRepository.findByCategoryIn(eq(categories), eq(pageable))).thenReturn(createMockVideoSearchPage());

        // Act
        Page<VideoSearchDocument> result = videoService.findVideoRecommendations(categories);

        // Assert
        assertFalse(result.isEmpty());
        verify(videoSearchRepository, times(1)).findByCategoryIn(eq(categories), eq(pageable));
    }

    @Test
    void toVideo_shouldConvertVideoRequestDTOToVideoDocument() {
        // Arrange
        VideoRequestDTO requestDTO = createMockVideoRequestDTO();

        // Act
        VideoDocument result = videoService.toVideo(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(requestDTO.getTitle(), result.getTitle());
        assertEquals(requestDTO.getDescription(), result.getDescription());
        assertEquals(requestDTO.getCategory(), result.getCategory());
        // Verify other properties
    }

    @Test
    void toVideoResponse_shouldConvertVideoDocumentToVideoResponseDTO() {
        // Arrange
        VideoDocument videoDocument = createMockVideoDocument();

        // Act
        VideoResponseDTO result = videoService.toVideoResponse(videoDocument);

        // Assert
        assertNotNull(result);
        assertEquals(videoDocument.getTitle(), result.getTitle());
        assertEquals(videoDocument.getDescription(), result.getDescription());
        // Verify other properties
    }

    @Test
    void videoSearchToVideoResponse_shouldConvertVideoSearchDocumentToVideoResponseDTO() {
        // Arrange
        VideoSearchDocument videoSearchDocument = createMockVideoSearchDocument();

        // Act
        VideoResponseDTO result = videoService.videoSearchToVideoResponse(videoSearchDocument);

        // Assert
        assertNotNull(result);
        assertEquals(videoSearchDocument.getTitle(), result.getTitle());
        assertEquals(videoSearchDocument.getDescription(), result.getDescription());
        // Verify other properties
    }

    @Test
    void videoDocumentToVideoSearchDocument_shouldConvertVideoDocumentToVideoSearchDocument() {
        // Arrange
        VideoDocument videoDocument = createMockVideoDocument();

        // Act
        VideoSearchDocument result = videoService.videoDocumentToVideoSearchDocument(videoDocument);

        // Assert
        assertNotNull(result);
        assertEquals(videoDocument.getTitle(), result.getTitle());
        assertEquals(videoDocument.getDescription(), result.getDescription());
        // Verify other properties
    }

    private VideoSearchDocument createMockVideoSearchDocument() {
        VideoSearchDocument videoSearchDocument = new VideoSearchDocument();
        videoSearchDocument.setTitle("Test Title");
        videoSearchDocument.setDescription("Test Description");
        videoSearchDocument.setCategory("Test Category");
        // Set other properties
        return videoSearchDocument;
    }

    // Add more test cases for other methods

    private Page<VideoSearchDocument> createMockVideoSearchPage() {
        VideoSearchDocument videoSearchDocument = new VideoSearchDocument();
        return new PageImpl<>(Collections.singletonList(videoSearchDocument));
    }

    private VideoRequestDTO createMockVideoRequestDTO() {
        VideoRequestDTO requestDTO = new VideoRequestDTO();
        requestDTO.setTitle("Test Title");
        // Set other properties
        return requestDTO;
    }

    private VideoDocument createMockVideoDocument() {
        VideoDocument videoDocument = new VideoDocument();
        videoDocument.setTitle("Test Title");
        // Set other properties
        return videoDocument;
    }
}
