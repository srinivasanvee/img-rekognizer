package com.img.upload.manageimages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ManageImagesApplication {
	public static void main(String[] args) throws IOException {
		SpringApplication.run(ManageImagesApplication.class, args);
	}
}

/*
TODO:
- Dependency injection with interfaces
- Write unit tests
- Automation script to load all the images
- UI to search the results and display images
- Add docker support with volume mapping
 - Error handling
 - Validation
 - Readme.md with instructions
 - Build UI for search
- Openapi doc
 */
