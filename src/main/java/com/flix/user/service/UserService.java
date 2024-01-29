package com.flix.user.service;

import com.flix.aggregator.service.AggregateService;
import com.flix.shared.exception.models.VideoException;
import com.flix.user.dto.request.UserRequestDTO;
import com.flix.user.dto.response.UserResponseDTO;
import com.flix.user.persistence.entity.FavoriteVideos;
import com.flix.user.persistence.entity.UserDocument;
import com.flix.user.persistence.repository.UserDocumentRepository;
import com.flix.video.dto.response.VideoResponseDTO;
import com.flix.video.persistence.entity.VideoDocument;
import com.flix.video.persistence.entity.VideoSearchDocument;
import com.flix.video.service.VideoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserDocumentRepository userDocumentRepository;

    @Autowired
    private VideoService videoService;

    @Autowired
    private AggregateService aggregateService;

    public UserResponseDTO addUser(UserRequestDTO userRequestDTO) {

        UserDocument userDocument = userDocumentRepository.save(toUser(userRequestDTO));

        return toUserResponse(userDocument);
    }

    public UserResponseDTO updateUser(UserRequestDTO user, String idUser) {
        UserDocument userDocument = getUser(idUser);

        if (user.getName() != null) {
            userDocument.setName(user.getName());
        }

        if (user.getEmail() != null) {
            userDocument.setEmail(user.getEmail());
        }

        UserDocument userUpdated = userDocumentRepository.save(userDocument);

        return toUserResponse(userUpdated);
    }

    @Transactional
    public void addFavoriteVideo(String idUser, String idVideo) {
        UserDocument userDocument = getUser(idUser);
        List<FavoriteVideos> favoriteVideos = new ArrayList<>(userDocument.getFavoriteVideos());

        VideoDocument videoDocument = videoService.findVideo(idVideo);
        FavoriteVideos newFavoriteVideo = toFavoriteVideo(videoDocument);

        favoriteVideos.add(newFavoriteVideo);

        userDocument.setFavoriteVideos(favoriteVideos);
        userDocumentRepository.save(userDocument);

        aggregateService.aggregateFavoriteVideo();
    }



    @Transactional
    public void removeUser(String idUser) {
        userDocumentRepository.deleteById(idUser);
    }

    public Page<VideoResponseDTO> getRecommendations(String idUser) {
        UserDocument userDocument = getUser(idUser);
        List<FavoriteVideos> favoriteVideos = userDocument.getFavoriteVideos();

        if (favoriteVideos.isEmpty()) {
            return Page.empty();
        }

        Set<String> allCategories = favoriteVideos.stream()
                .map(FavoriteVideos::getCategory)
                .collect(Collectors.toSet());

        Page<VideoSearchDocument> videoRecommendations = videoService.findVideoRecommendations(allCategories);
        return videoRecommendations.map(this::videoSearchToVideoResponse);
    }

    public UserResponseDTO findUser(String idUser) {
        return toUserResponse(userDocumentRepository.findById(idUser).orElseThrow(() -> new VideoException("User not found")));
    }

    private UserDocument getUser(String idUser) {
        return userDocumentRepository.findById(idUser).orElseThrow(() -> new VideoException("User not found"));
    }

    private UserDocument toUser(UserRequestDTO userRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(userRequestDTO, UserDocument.class);
    }

    private UserResponseDTO toUserResponse(UserDocument userDocument) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(userDocument, UserResponseDTO.class);
    }

    private FavoriteVideos toFavoriteVideo(VideoDocument videoDocument) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(videoDocument, FavoriteVideos.class);
    }

    private VideoResponseDTO videoSearchToVideoResponse(VideoSearchDocument videoSearchDocument) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(videoSearchDocument, VideoResponseDTO.class);
    }
}
