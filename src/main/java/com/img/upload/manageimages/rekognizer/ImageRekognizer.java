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

    public List<String> getLabelsByFilePath(String filePath) throws IOException {
        var input = new FileInputStream(filePath).readAllBytes();
        Image img = new Image().withBytes(ByteBuffer.wrap(input));
        return this.rekognize(img);
    }

    public List<String> getLabelsByMultiPartFile(MultipartFile file) throws IOException {
        Image img = new Image().withBytes(ByteBuffer.wrap(file.getBytes()));
        return this.rekognize(img);
    }

    public List<String> getLabelsByS3Obj(String bucket, String file) throws IOException {
        Image img = new Image().withS3Object(new S3Object().withName(file).withBucket(bucket));
        return this.rekognize(img);
    }

    private List<String> rekognize(Image image) throws IOException {

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(image)
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
