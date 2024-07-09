package com.gregorybroche.imageresolver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class ImageresolverApplication {

	public static void main(String[] args) {
		// SpringApplication.run(ImageresolverApplication.class, args);
		Application.launch(JavaFxApp.class, args);
	}

}
