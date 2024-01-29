package com.flix.aggregator.persistence.repository;

import com.flix.aggregator.persistence.entity.AggregatorDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AggregatorDocumentRepository extends MongoRepository<AggregatorDocument, String>  {
}
