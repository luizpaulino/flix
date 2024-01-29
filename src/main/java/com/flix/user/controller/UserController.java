package com.flix.user.controller;

import com.flix.user.dto.request.UserRequestDTO;
import com.flix.user.dto.response.UserResponseDTO;
import com.flix.user.service.UserService;
import com.flix.video.dto.response.VideoResponseDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @PutMapping("/{idUser}")
    public ResponseEntity<UserResponseDTO> updateVideo(
            @Valid @RequestBody UserRequestDTO user,
            @PathVariable String idUser
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(user, idUser));
    }

    @DeleteMapping("/{idUser}")
    public ResponseEntity<Void> removeVideo(@PathVariable String idUser) {
        userService.removeUser(idUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{idUser}")
    public UserResponseDTO findUser(@PathVariable String idUser) {
        return userService.findUser(idUser);
    }

    @PostMapping("/{idUser}/favorite/{idVideo}")
    public ResponseEntity<Void> addFavoriteVideo(
            @PathVariable String idUser,
            @PathVariable String idVideo
    ) {
        userService.addFavoriteVideo(idUser, idVideo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{idUser}/recommendations")
    public Page<VideoResponseDTO> getRecommendations (@PathVariable String idUser) {
        return userService.getRecommendations(idUser);
    }
}
