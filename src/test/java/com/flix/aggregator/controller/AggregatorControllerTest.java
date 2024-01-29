package com.flix.aggregator.controller;

import com.flix.aggregator.dto.response.AggregatorResponseDTO;
import com.flix.aggregator.service.AggregateService;
import com.flix.user.dto.response.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AggregatorControllerTest {

    @Mock
    private AggregateService aggregateService;

    @InjectMocks
    private AggregatorController aggregatorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void aggregateFavoriteVideo_shouldReturnOkResponse() {
        // Arrange

        // Act
        ResponseEntity<UserResponseDTO> responseEntity = aggregatorController.aggregateFavoriteVideo();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(aggregateService, times(1)).aggregateWatchedVideo();
    }

    @Test
    void getAggregators_shouldReturnOkResponseWithAggregatorResponseDTO() {
        // Arrange
        AggregatorResponseDTO expectedResponse = new AggregatorResponseDTO();
        when(aggregateService.getAggregates()).thenReturn(expectedResponse);

        // Act
        ResponseEntity<AggregatorResponseDTO> responseEntity = aggregatorController.getAggregators();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(aggregateService, times(1)).getAggregates();
    }
}
