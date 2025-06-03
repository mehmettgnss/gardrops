package com.gardrops.imageupload.controller;

import com.gardrops.imageupload.dto.ImageResponse;
import com.gardrops.imageupload.dto.ImagesListResponse;
import com.gardrops.imageupload.dto.SessionResponse;
import com.gardrops.imageupload.service.SessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final RestTemplate restTemplate;
    private final String imageProcessingServiceUrl;

    public SessionController(SessionService sessionService, RestTemplate restTemplate,
            @Value("${app.image-processing.service-url}") String imageProcessingServiceUrl) {
        this.sessionService = sessionService;
        this.restTemplate = restTemplate;
        this.imageProcessingServiceUrl = imageProcessingServiceUrl;
    }

    @PostMapping
    public ResponseEntity<SessionResponse> createSession() {
        UUID sessionId = sessionService.createSession();
        return ResponseEntity.ok(new SessionResponse(sessionId));
    }

    @PostMapping("/{sessionId}/images")
    public ResponseEntity<ImageResponse> uploadImage(
            @PathVariable UUID sessionId,
            @RequestParam("image") MultipartFile image) throws IOException {
        
        if (!sessionService.isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!sessionService.canAddImage(sessionId)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        UUID imageId = UUID.randomUUID();
        String destinationPath = String.format("/tmp/uploads/%s/%s.jpg", sessionId, imageId);
        
        Path directory = Paths.get("/tmp/uploads/" + sessionId);
        Files.createDirectories(directory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        });
        body.add("destinationFilePath", destinationPath);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(imageProcessingServiceUrl + "/images/process", requestEntity, Void.class);

        sessionService.addImage(sessionId, imageId);
        return ResponseEntity.ok(new ImageResponse(imageId));
    }

    @DeleteMapping("/{sessionId}/images/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable UUID sessionId,
            @PathVariable UUID imageId) throws IOException {
        
        if (!sessionService.isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Path imagePath = Paths.get(String.format("/tmp/uploads/%s/%s.jpg", sessionId, imageId));
        Files.deleteIfExists(imagePath);
        sessionService.removeImage(sessionId, imageId);
        
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{sessionId}/images")
    public ResponseEntity<ImagesListResponse> listImages(@PathVariable UUID sessionId) {
        if (!sessionService.isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(new ImagesListResponse(new java.util.ArrayList<>(sessionService.getSessionImages(sessionId))));
    }
} 