package com.img.upload.manageimages;

import com.img.upload.manageimages.rekognizer.ImageRekognizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ManageImagesApplication {
	public static void main(String[] args) throws IOException {
		/*
		ImageRekognizer recog = new ImageRekognizer("/Users/srinivasanvaradharajan/Projects/FileStore/20210916_200830.jpg");
		var res = recog.rekognize();
		System.out.println("==========================");
		for(String s: res)
		{
			System.out.println(s);
		}
		System.out.println("==========================");
		 */
		SpringApplication.run(ManageImagesApplication.class, args);
	}
}
