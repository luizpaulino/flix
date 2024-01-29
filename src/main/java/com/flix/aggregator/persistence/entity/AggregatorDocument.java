package com.flix.aggregator.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "aggregator")
public class AggregatorDocument {

    @Id
    private String id = UUID.randomUUID().toString();
    private int totalVideos;
    private int totalFavorites;
    private int totalWatched;
}
