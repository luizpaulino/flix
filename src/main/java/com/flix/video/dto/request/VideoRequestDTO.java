package com.flix.video.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VideoRequestDTO {

    @JsonProperty("title")
    @NotBlank(message = "title is mandatory")
    @Size(min = 2, message = "title must have at least 2 characters")
    private String title;

    @JsonProperty("description")
    @NotBlank(message = "description is mandatory")
    @Size(min = 5, message = "title must have at least 5 characters")
    private String description;

    @JsonProperty("category")
    @NotBlank(message = "category is mandatory")
    @Size(min = 2, message = "title must have at least 2 characters")
    private String category;
}
