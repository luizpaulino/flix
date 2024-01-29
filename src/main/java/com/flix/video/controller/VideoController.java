package com.flix.video.controller;

import com.flix.video.dto.request.VideoRequestDTO;
import com.flix.video.dto.response.VideoResponseDTO;
import com.flix.video.service.VideoService;
import com.flix.video.service.VideoUploadService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoUploadService videoUploadService;

    @GetMapping
    public Page<VideoResponseDTO> searchVideos(
            @RequestParam(value = "field", required = false)
            String field,
            @RequestParam(value = "query", required = false)
            String query,
            @RequestParam(value = "direction", required = false, defaultValue = "DESC")
            String direction,
            Pageable pageable) {
        return videoService.searchVideos(field, query, direction, pageable);
    }
    @PostMapping("/{idVideo}/upload")
    public ResponseEntity<Void> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @PathVariable String idVideo
    ) throws IOException {
        videoUploadService.uploadVideo(file, idVideo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{idVideo}")
    public ResponseEntity<VideoResponseDTO> updateVideo(
            @Valid @RequestBody VideoRequestDTO video,
            @PathVariable String idVideo
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(videoService.updateVideo(video, idVideo));
    }

    @DeleteMapping("/{idVideo}")
    public ResponseEntity<Void> removeVideo(
            @PathVariable String idVideo
    ) {
        videoService.removeVideo(idVideo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VideoResponseDTO> addVideo(@Valid @RequestBody VideoRequestDTO video){
        return ResponseEntity.status(HttpStatus.CREATED).body(videoService.addVideo(video));
    }
}
