package com.img.upload.manageimages.rekognizer;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ImageRekognizer {
    List<String> res_labels = new ArrayList<>();
    private MultipartFile fileToProcess;
    private String filePath;

    private byte[] imageBytes;

    public ImageRekognizer() {}

    public ImageRekognizer(String filePath)
    {
        this.filePath = filePath;
    }

    public ImageRekognizer(byte[] bytes)
    {
        this.imageBytes = bytes;
    }

    public ImageRekognizer(MultipartFile file)
    {
        this.fileToProcess = file;
    }

    private Image getImage() throws IOException {
        if (fileToProcess != null)
        {
            return new Image().withBytes(ByteBuffer.wrap(this.fileToProcess.getBytes()));
        }
        else if(filePath != null) {
            var input = new FileInputStream(this.filePath).readAllBytes();
            return new Image().withBytes(ByteBuffer.wrap(input));
        }
        {
            String photo = "cat_img.png";
            String bucket = "sri-photo-bucket";
            return new Image().withS3Object(new S3Object().withName(photo).withBucket(bucket));
        }
    }

    public List<String> rekognize() throws IOException {

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(getImage())
                .withMaxLabels(10).withMinConfidence(75F);

        try {
            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List<Label> labels = result.getLabels();
            System.out.println("Detected labels for the image");

            for (Label label : labels) {

                System.out.println("Label: " + label.getName());
                System.out.println("Confidence: " + label.getConfidence().toString() + "\n");
                if(!res_labels.contains(label.getName()))
                    res_labels.add(label.getName());

                List<Instance> instances = label.getInstances();
                System.out.println("Instances of " + label.getName());
                if (instances.isEmpty()) {
                    System.out.println("  " + "None");
                } else {
                    for (Instance instance : instances) {
                        System.out.println("  Confidence: " + instance.getConfidence().toString());
                        System.out.println("  Bounding box: " + instance.getBoundingBox().toString());
                    }
                }
                System.out.println("Parent labels for " + label.getName() + ":");
                List<Parent> parents = label.getParents();
                if (parents.isEmpty()) {
                    System.out.println("  None");
                } else {
                    for (Parent parent : parents) {
                        System.out.println("  " + parent.getName());
                        if(!res_labels.contains(parent.getName()))
                            res_labels.add(parent.getName());
                    }
                }
                System.out.println("--------------------");
                System.out.println();
            }
        } catch (AmazonRekognitionException e) {
            e.printStackTrace();
        }
        return res_labels;
    }
}
