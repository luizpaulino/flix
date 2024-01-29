package com.flix.aggregator.controller;

import com.flix.aggregator.dto.response.AggregatorResponseDTO;
import com.flix.aggregator.service.AggregateService;
import com.flix.user.dto.response.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aggregators")
public class AggregatorController {

    @Autowired
    private AggregateService aggregateService;

    @PostMapping("/watched")
    public ResponseEntity<UserResponseDTO> aggregateFavoriteVideo() {
        aggregateService.aggregateWatchedVideo();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<AggregatorResponseDTO> getAggregators() {
        return ResponseEntity.status(HttpStatus.OK).body(aggregateService.getAggregates());
    }
}
