package com.img.upload.manageimages.images;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class ImageProcessor {
    String FILE_PATH = "/Users/srinivasanvaradharajan/Projects/FileStore/";

    public Image saveImage(MultipartFile file) throws IOException {

        String filePath = FILE_PATH + file.getOriginalFilename();

        file.transferTo(new File(filePath));

        Image img = new Image();
        img.setFileName(file.getOriginalFilename());
        img.setFilePath(filePath);
        img.setFileSize(String.valueOf(file.getSize()));

        return img;
    }

    public byte[] getImage(String filePath) throws IOException {
        byte[] image = Files.readAllBytes(new File(filePath).toPath());
        return image;
    }
}
