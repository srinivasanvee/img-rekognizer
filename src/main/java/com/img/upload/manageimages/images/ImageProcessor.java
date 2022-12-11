package com.img.upload.manageimages.images;
import com.img.upload.manageimages.rekognizer.ImageRekognizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class ImageProcessor {
    @Value("${app.manage.images.filestore}")
    private String FILE_PATH;

    public Image saveImage(MultipartFile file) throws IOException {
        String filePath = FILE_PATH + file.getOriginalFilename();

        file.transferTo(new File(filePath));

        Image img = new Image();
        img.setFileName(file.getOriginalFilename());
        img.setFilePath(filePath);
        img.setFileSize(String.valueOf(file.getSize()));
        img.setObjectType(getObjectTypes(filePath));

        return img;
    }

    private String getObjectTypes(String filePath) throws IOException {
        var reko = new ImageRekognizer();
        var res = reko.getLabelsByFilePath(filePath);
        var result = String.join(",",res);
        return result;
    }

    public byte[] getImage(String filePath) throws IOException {
        byte[] image = Files.readAllBytes(new File(filePath).toPath());
        return image;
    }
}
