package com.flix.user.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FavoriteVideos {
    private String id;
    private String title;
    private String category;
}
