package com.flix.user.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "users")
public class UserDocument {

    @Id
    private String id = UUID.randomUUID().toString();
    private String name;
    private String email;
    private List<FavoriteVideos> favoriteVideos = List.of();
}
