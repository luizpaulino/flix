package com.flix.video.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "videos")
public class VideoDocument {

    @Id
    private String id = UUID.randomUUID().toString();
    private String title;
    private String description;
    private String url;
    private String category;
    private LocalDate publicationDate = LocalDate.now();
}
