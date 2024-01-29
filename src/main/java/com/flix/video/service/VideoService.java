package com.flix.video.service;

import com.flix.shared.exception.models.VideoException;
import com.flix.video.dto.request.VideoRequestDTO;
import com.flix.video.dto.response.VideoResponseDTO;
import com.flix.video.persistence.entity.VideoDocument;
import com.flix.video.persistence.entity.VideoSearchDocument;
import com.flix.video.persistence.repository.VideoDocumentRepository;
import com.flix.video.persistence.repository.VideoSearchRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class VideoService {

    @Autowired
    private VideoSearchRepository videoSearchRepository;

    @Autowired
    private VideoDocumentRepository videoDocumentRepository;

    public Page<VideoResponseDTO> searchVideos(String field, String query, String direction, Pageable pageable) {

        Sort sort = Sort.by(Sort.Order.by("publicationDate").with(Sort.Direction.fromString(direction)));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        if (!StringUtils.hasText(query) || !StringUtils.hasText(field)) {
            return videoSearchRepository.findAll(pageable).map(this::videoSearchToVideoResponse);
        }

        Page<VideoSearchDocument> videos;
        switch (field.toLowerCase()) {
            case "title" -> videos = videoSearchRepository.findByTitle(query, pageable);
            case "category" -> videos = videoSearchRepository.findByCategory(query, pageable);
            case "publication_date" -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate publicationDateParsed = LocalDate.parse(query, formatter);
                videos = videoSearchRepository.findByPublicationDate(publicationDateParsed, pageable);
            }
            default -> videos = Page.empty();
        }

        return videos.map(this::videoSearchToVideoResponse);
    }

    public VideoResponseDTO addVideo(VideoRequestDTO videoRequestDTO) {

        VideoDocument videoDocument = videoDocumentRepository.save(toVideo(videoRequestDTO));
        return toVideoResponse(videoDocument);
    }

    public VideoResponseDTO updateVideo(VideoRequestDTO videoRequestDTO, String idVideo) {
        VideoDocument videoDocument = findVideo(idVideo);

        if (videoRequestDTO.getTitle() != null) {
            videoDocument.setTitle(videoRequestDTO.getTitle());
        }

        if (videoRequestDTO.getDescription() != null) {
            videoDocument.setDescription(videoRequestDTO.getDescription());
        }

        if (videoRequestDTO.getCategory() != null) {
            videoDocument.setCategory(videoRequestDTO.getCategory());
        }

        videoSearchRepository.deleteById(idVideo);

        videoDocumentRepository.save(videoDocument);

        videoSearchRepository.save(videoDocumentToVideoSearchDocument(videoDocument));

        return toVideoResponse(videoDocument);
    }

    public void removeVideo(String idVideo) {
        VideoDocument videoDocument = findVideo(idVideo);
        videoSearchRepository.deleteById(idVideo);
        videoDocumentRepository.delete(videoDocument);
    }

    public VideoDocument findVideo(String idVideo) {
        return videoDocumentRepository.findById(idVideo).orElseThrow(() -> new VideoException("Video not found"));
    }

    public Page<VideoSearchDocument> findVideoRecommendations(Set<String> categories) {
        return videoSearchRepository.findByCategoryIn(categories, PageRequest.of(0, 10));
    }

    VideoDocument toVideo(VideoRequestDTO videoRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(videoRequestDTO, VideoDocument.class);
    }

    VideoResponseDTO toVideoResponse(VideoDocument videoDocument) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(videoDocument, VideoResponseDTO.class);
    }

    VideoResponseDTO videoSearchToVideoResponse(VideoSearchDocument videoSearchDocument) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(videoSearchDocument, VideoResponseDTO.class);
    }

    VideoSearchDocument videoDocumentToVideoSearchDocument(VideoDocument videoDocument) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(videoDocument, VideoSearchDocument.class);
    }
}