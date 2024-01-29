package com.flix.aggregator.service;

import com.flix.aggregator.dto.response.AggregatorResponseDTO;
import com.flix.aggregator.persistence.entity.AggregatorDocument;
import com.flix.aggregator.persistence.repository.AggregatorDocumentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AggregateService {

    @Autowired
    private AggregatorDocumentRepository aggregatorDocumentRepository;

    private static final  String ID_AGGREGATOR = "AGGREGATOR";

    public void aggregateFavoriteVideo() {
        AggregatorDocument aggregatorDocument = aggregatorDocumentRepository.findById(ID_AGGREGATOR).orElseThrow();
        aggregatorDocument.setTotalFavorites(aggregatorDocument.getTotalFavorites() + 1);

        aggregatorDocumentRepository.save(aggregatorDocument);
    }

    public void aggregateTotalVideo() {
        AggregatorDocument aggregatorDocument = aggregatorDocumentRepository.findById(ID_AGGREGATOR).orElseThrow();
        aggregatorDocument.setTotalVideos(aggregatorDocument.getTotalVideos() + 1);

        aggregatorDocumentRepository.save(aggregatorDocument);
    }

    public void aggregateWatchedVideo() {
        AggregatorDocument aggregatorDocument = aggregatorDocumentRepository.findById(ID_AGGREGATOR).orElseThrow();
        aggregatorDocument.setTotalWatched(aggregatorDocument.getTotalWatched() + 1);

        aggregatorDocumentRepository.save(aggregatorDocument);
    }

    public AggregatorResponseDTO getAggregates() {
        AggregatorDocument aggregatorDocument = aggregatorDocumentRepository.findById(ID_AGGREGATOR).orElseThrow();
        AggregatorResponseDTO aggregatorResponse = toAggregatorResponse(aggregatorDocument);

        if (aggregatorDocument.getTotalVideos() == 0 || aggregatorDocument.getTotalWatched() == 0) {
            aggregatorResponse.setAverageViews(0);
            return aggregatorResponse;
        }

        aggregatorResponse.setAverageViews(aggregatorDocument.getTotalWatched() / aggregatorDocument.getTotalVideos());

        return aggregatorResponse;
    }

    private AggregatorResponseDTO toAggregatorResponse(AggregatorDocument aggregatorDocument) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(aggregatorDocument, AggregatorResponseDTO.class);
    }
}
