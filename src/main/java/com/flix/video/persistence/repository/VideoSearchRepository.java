package com.flix.video.persistence.repository;

import com.flix.video.persistence.entity.VideoSearchDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDate;
import java.util.Set;

public interface VideoSearchRepository extends ElasticsearchRepository<VideoSearchDocument, String> {
    Page<VideoSearchDocument> findByTitle(String title, Pageable pageable);

    Page<VideoSearchDocument> findByPublicationDate(LocalDate publicationDate, Pageable pageable);

    Page<VideoSearchDocument> findByCategory(String category, Pageable pageable);

    Page<VideoSearchDocument> findAll(Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"terms\": {\"category.keyword\": ?0}}]}}")
    Page<VideoSearchDocument> findByCategoryIn(Set<String> categories, Pageable pageable);
}

