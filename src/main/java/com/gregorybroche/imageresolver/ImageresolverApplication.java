package com.gregorybroche.imageresolver;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import javafx.application.Application;

@SpringBootApplication
@PropertySource("classpath:custom.properties")
public class ImageresolverApplication {

	public static void main(String[] args) {
		Application.launch(JavaFxApp.class, args);
	}
}
