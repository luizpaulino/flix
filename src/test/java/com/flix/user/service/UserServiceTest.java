package com.flix.user.service;

import com.flix.aggregator.service.AggregateService;
import com.flix.shared.exception.models.VideoException;
import com.flix.user.dto.request.UserRequestDTO;
import com.flix.user.dto.response.UserResponseDTO;
import com.flix.user.persistence.entity.FavoriteVideos;
import com.flix.user.persistence.entity.UserDocument;
import com.flix.user.persistence.repository.UserDocumentRepository;
import com.flix.video.dto.response.VideoResponseDTO;
import com.flix.video.persistence.entity.VideoDocument;
import com.flix.video.persistence.entity.VideoSearchDocument;
import com.flix.video.service.VideoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDocumentRepository userDocumentRepository;

    @Mock
    private VideoService videoService;

    @Mock
    private AggregateService aggregateService;

    @InjectMocks
    private UserService userService;

    private final ModelMapper modelMapper = new ModelMapper();


    @Test
    void addFavoriteVideo_shouldAddFavoriteVideoAndAggregate() {
        // Arrange
        String userId = "userId";
        String videoId = "videoId";

        UserDocument userDocument = new UserDocument();
        VideoDocument videoDocument = new VideoDocument();
        videoDocument.setId(videoId);

        when(userDocumentRepository.findById(userId)).thenReturn(Optional.of(userDocument));
        when(videoService.findVideo(videoId)).thenReturn(videoDocument);

        // Act
        userService.addFavoriteVideo(userId, videoId);

        // Assert
        assertTrue(userDocument.getFavoriteVideos().stream().anyMatch(fv -> fv.getId().equals(videoId)));
        verify(userDocumentRepository, times(1)).save(userDocument);
        verify(videoService, times(1)).findVideo(videoId);
        verify(videoService, times(1)).findVideo(videoId);
    }

    @Test
    void addFavoriteVideo_shouldThrowVideoExceptionWhenUserNotFound() {
        // Arrange
        String userId = "userId";
        String videoId = "videoId";

        when(userDocumentRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(VideoException.class, () -> userService.addFavoriteVideo(userId, videoId));
        verify(videoService, never()).findVideo(anyString());
    }

    @Test
    void removeUser_shouldRemoveUser() {
        // Arrange
        String userId = "userId";

        // Act
        assertDoesNotThrow(() -> userService.removeUser(userId));

        // Verify that deleteById was called with the correct userId
        verify(userDocumentRepository).deleteById(userId);

        // Verify that findById was never called
        verify(userDocumentRepository, never()).findById(anyString());
    }


    @Test
    void getRecommendations_shouldReturnPageOfVideoResponseDTO() {
        // Arrange
        String userId = "userId";
        UserDocument userDocument = new UserDocument();
        userDocument.setId(userId);

        FavoriteVideos favoriteVideo = new FavoriteVideos();
        favoriteVideo.setCategory("Action");

        userDocument.setFavoriteVideos(Collections.singletonList(favoriteVideo));

        VideoSearchDocument videoSearchDocument = new VideoSearchDocument();
        videoSearchDocument.setTitle("Action Movie");

        PageImpl<VideoSearchDocument> videoPage = new PageImpl<>(Collections.singletonList(videoSearchDocument));

        when(userDocumentRepository.findById(userId)).thenReturn(Optional.of(userDocument));
        when(videoService.findVideoRecommendations(Collections.singleton(favoriteVideo.getCategory()))).thenReturn(videoPage);

        // Act
        Page<VideoResponseDTO> recommendations = userService.getRecommendations(userId);

        // Assert
        assertEquals(1, recommendations.getTotalElements());
        assertEquals(videoSearchDocument.getTitle(), recommendations.getContent().get(0).getTitle());
        verify(userDocumentRepository, times(1)).findById(userId);
        verify(videoService, times(1)).findVideoRecommendations(Collections.singleton(favoriteVideo.getCategory()));
    }

    @Test
    void getRecommendations_shouldReturnEmptyPageWhenUserHasNoFavoriteVideos() {
        // Arrange
        String userId = "userId";
        UserDocument userDocument = new UserDocument();
        userDocument.setId(userId);

        when(userDocumentRepository.findById(userId)).thenReturn(Optional.of(userDocument));

        // Act
        Page<VideoResponseDTO> recommendations = userService.getRecommendations(userId);

        // Assert
        assertTrue(recommendations.isEmpty());
        verify(userDocumentRepository, times(1)).findById(userId);
        verify(videoService, never()).findVideoRecommendations(any());
    }

    @Test
    void findUser_shouldReturnUserResponseDTO() {
        // Arrange
        String userId = "userId";
        UserDocument userDocument = new UserDocument();
        userDocument.setId(userId);

        when(userDocumentRepository.findById(userId)).thenReturn(Optional.of(userDocument));

        // Act
        UserResponseDTO userResponseDTO = userService.findUser(userId);

        // Assert
        assertNotNull(userResponseDTO);
        assertEquals(userId, userResponseDTO.getId());
        verify(userDocumentRepository, times(1)).findById(userId);
    }

    @Test
    void findUser_shouldThrowVideoExceptionWhenUserNotFound() {
        // Arrange
        String userId = "userId";

        when(userDocumentRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(VideoException.class, () -> userService.findUser(userId));
        verify(userDocumentRepository, times(1)).findById(userId);
    }

    @Test
    void addUser_shouldAddUser() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("John Doe");
        userRequestDTO.setEmail("john.doe@example.com");

        UserDocument savedUser = new UserDocument();
        savedUser.setId("userId");
        savedUser.setName(userRequestDTO.getName());
        savedUser.setEmail(userRequestDTO.getEmail());

        when(userDocumentRepository.save(any(UserDocument.class))).thenReturn(savedUser);

        // Act
        UserResponseDTO userResponseDTO = userService.addUser(userRequestDTO);

        // Assert
        assertNotNull(userResponseDTO);
        assertEquals(savedUser.getId(), userResponseDTO.getId());
        assertEquals(savedUser.getName(), userResponseDTO.getName());
        assertEquals(savedUser.getEmail(), userResponseDTO.getEmail());

        verify(userDocumentRepository, times(1)).save(any(UserDocument.class));
    }

    @Test
    void updateUser_shouldUpdateUser() {
        // Arrange
        String userId = "userId";
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("Updated Name");

        UserDocument existingUser = new UserDocument();
        existingUser.setId(userId);
        existingUser.setName("Old Name");
        existingUser.setEmail("old.email@example.com");

        when(userDocumentRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userDocumentRepository.save(any(UserDocument.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserResponseDTO updatedUser = userService.updateUser(userRequestDTO, userId);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.getId());
        assertEquals(userRequestDTO.getName(), updatedUser.getName());
        assertEquals(existingUser.getEmail(), updatedUser.getEmail());

        verify(userDocumentRepository, times(1)).findById(userId);
        verify(userDocumentRepository, times(1)).save(any(UserDocument.class));
    }
}

