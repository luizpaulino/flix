package com.flix.video.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Document(indexName = "videos")
public class VideoSearchDocument {

    @Id
    private String id = UUID.randomUUID().toString() ;
    private String title;
    private String description;
    private String url;
    private String category;
    @Field(type = FieldType.Date, pattern = "dd/MM/yyyy")
    private LocalDate publicationDate;

}

