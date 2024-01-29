package com.flix.aggregator.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AggregatorResponseDTO {

    @JsonProperty("total_videos")
    private int totalVideos;

    @JsonProperty("total_favorites")
    private int totalFavorites;

    @JsonProperty("total_watched")
    private int totalWatched;

    @JsonProperty("average_views")
    private int averageViews;
}