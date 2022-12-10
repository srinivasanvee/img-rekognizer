package com.img.upload.manageimages.images;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/*
TODO:
 - Image upload
 - Image Processing
 - Dependency injection
 - Unit testing
 - Error handling
 - Validation
 - Readme.md with instructions
 - Build UI for demo
  */

@RestController
public class ImageController {

    private ImageProcessor imageProcessor;
    private ImageRepository imageRepository;

    public ImageController(ImageRepository imageRepository, ImageProcessor imageProcessor) {
        this.imageRepository = imageRepository;
        this.imageProcessor = imageProcessor;
    }

    @GetMapping(path = "/images")
    public List<Image> getImages() {
        return this.imageRepository.findAll();
    }

    @GetMapping(path = "/images/{id}")
    public Image getImageById(@PathVariable int id) {
        return this.imageRepository.findById(id).get();
    }

    @PostMapping(path = "/image")
    public ResponseEntity<Image> postRawImage(@RequestParam("image")MultipartFile file) throws IOException {
        Image image = this.imageProcessor.saveImage(file);
        var resp = this.imageRepository.save(image);
        var url = "/images/" + resp.getId().toString();
        return ResponseEntity.created(URI.create(url)).build();
    }
}