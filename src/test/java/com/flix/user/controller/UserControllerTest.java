package com.flix.user.controller;

import com.flix.user.dto.request.UserRequestDTO;
import com.flix.user.dto.response.UserResponseDTO;
import com.flix.user.service.UserService;
import com.flix.video.dto.response.VideoResponseDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private Validator validator;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addUser_shouldReturnCreated() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("John Doe");
        userRequestDTO.setEmail("john@example.com");

        // Act
        ResponseEntity<UserResponseDTO> responseEntity = userController.addUser(userRequestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(userService, times(1)).addUser(userRequestDTO);
    }

    @Test
    void updateVideo_shouldReturnOk() {
        // Arrange
        String idUser = "123";
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("John Doe");
        userRequestDTO.setEmail("john@example.com");

        // Act
        ResponseEntity<UserResponseDTO> responseEntity = userController.updateVideo(userRequestDTO, idUser);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).updateUser(userRequestDTO, idUser);
    }

    @Test
    void addFavoriteVideo_shouldReturnOk() {
        // Arrange
        String idUser = "123";
        String idVideo = "456";

        // Act
        ResponseEntity<Void> responseEntity = userController.addFavoriteVideo(idUser, idVideo);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).addFavoriteVideo(idUser, idVideo);
    }

    // Exemplo para testar o método getRecommendations
    @Test
    void getRecommendations_shouldReturnPageOfVideoResponseDTO() {
        // Arrange
        String idUser = "123";

        // Mock do Page<VideoResponseDTO>
        Page<VideoResponseDTO> mockPage = mock(Page.class);
        when(userService.getRecommendations(idUser)).thenReturn(mockPage);

        // Act
        Page<VideoResponseDTO> result = userController.getRecommendations(idUser);

        // Assert
        assertEquals(mockPage, result);
        verify(userService, times(1)).getRecommendations(idUser);
    }
}
