package com.flix.video.persistence.repository;

import com.flix.video.persistence.entity.VideoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface VideoDocumentRepository extends MongoRepository<VideoDocument, String>  {
}
