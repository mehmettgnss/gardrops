package com.gardrops.imageprocessing.controller;

import com.gardrops.imageprocessing.service.ImageProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/images")
public class ImageProcessingController {

    private final ImageProcessingService imageProcessingService;

    public ImageProcessingController(ImageProcessingService imageProcessingService) {
        this.imageProcessingService = imageProcessingService;
    }

    @PostMapping("/process")
    public ResponseEntity<Void> processImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("destinationFilePath") String destinationFilePath) throws IOException {
        
        imageProcessingService.processAndSaveImage(image, destinationFilePath);
        return ResponseEntity.ok().build();
    }
} 