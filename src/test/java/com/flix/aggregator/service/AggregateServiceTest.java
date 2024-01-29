package com.flix.aggregator.service;

import com.flix.aggregator.dto.response.AggregatorResponseDTO;
import com.flix.aggregator.persistence.entity.AggregatorDocument;
import com.flix.aggregator.persistence.repository.AggregatorDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AggregateServiceTest {

    @Mock
    private AggregatorDocumentRepository aggregatorDocumentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AggregateService aggregateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void aggregateFavoriteVideo_shouldIncrementTotalFavorites() {
        // Arrange
        AggregatorDocument aggregatorDocument = new AggregatorDocument();
        when(aggregatorDocumentRepository.findById("AGGREGATOR")).thenReturn(Optional.of(aggregatorDocument));

        // Act
        aggregateService.aggregateFavoriteVideo();

        // Assert
        assertEquals(1, aggregatorDocument.getTotalFavorites());
        verify(aggregatorDocumentRepository, times(1)).save(aggregatorDocument);
    }

    @Test
    void aggregateTotalVideo_shouldIncrementTotalVideos() {
        // Arrange
        AggregatorDocument aggregatorDocument = new AggregatorDocument();
        when(aggregatorDocumentRepository.findById("AGGREGATOR")).thenReturn(Optional.of(aggregatorDocument));

        // Act
        aggregateService.aggregateTotalVideo();

        // Assert
        assertEquals(1, aggregatorDocument.getTotalVideos());
        verify(aggregatorDocumentRepository, times(1)).save(aggregatorDocument);
    }

    @Test
    void aggregateWatchedVideo_shouldIncrementTotalWatched() {
        // Arrange
        AggregatorDocument aggregatorDocument = new AggregatorDocument();
        when(aggregatorDocumentRepository.findById("AGGREGATOR")).thenReturn(Optional.of(aggregatorDocument));

        // Act
        aggregateService.aggregateWatchedVideo();

        // Assert
        assertEquals(1, aggregatorDocument.getTotalWatched());
        verify(aggregatorDocumentRepository, times(1)).save(aggregatorDocument);
    }

    @Test
    void getAggregates_shouldReturnAggregatorResponseDTO() {
        // Arrange
        AggregatorDocument aggregatorDocument = new AggregatorDocument();
        when(aggregatorDocumentRepository.findById("AGGREGATOR")).thenReturn(Optional.of(aggregatorDocument));

        AggregatorResponseDTO expectedResponse = new AggregatorResponseDTO();
        when(modelMapper.map(aggregatorDocument, AggregatorResponseDTO.class)).thenReturn(expectedResponse);

        // Act
        AggregatorResponseDTO response = aggregateService.getAggregates();

        // Assert
        assertEquals(expectedResponse.getAverageViews(), response.getAverageViews());
    }
}
