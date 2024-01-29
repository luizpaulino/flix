package com.flix.user.persistence.repository;

import com.flix.user.persistence.entity.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDocumentRepository extends MongoRepository<UserDocument, String>  {
}
