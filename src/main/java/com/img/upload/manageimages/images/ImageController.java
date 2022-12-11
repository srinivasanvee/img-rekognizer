package com.img.upload.manageimages.images;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @PostMapping(path = "/images")
    public Image postRawImage(@RequestParam("image")MultipartFile file) throws IOException {
        Image image = this.imageProcessor.saveImage(file);
        var resp = this.imageRepository.save(image);
        return resp;
    }

    @GetMapping(path = "/images/search/{searchStr}")
    public List<Image> searchImages(@PathVariable String searchStr) throws IOException {
        String[] searchObj = searchStr.split(",");
        List<Image> result = new ArrayList<>();

        for(Image img: this.imageRepository.findAll()) {
            for(String search: searchObj) {
                if(img.getObjectType().toLowerCase().contains(search.toLowerCase())) {
                    result.add(img);
                }
            }
        }
        return result;
    }
}