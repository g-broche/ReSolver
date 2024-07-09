package com.gregorybroche.imageresolver;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JavaFxApp extends Application{
    static final String APP_NAME = "ReSolver";
    static final int APP_BASE_WIDTH = 1080;
    static final int APP_BASE_HEIGHT = 720;
    static final Color APP_BACKGROUND_COLOR = Color.valueOf("#193947");

    @Override
    public void start(Stage stage) throws Exception{
        Pane testPane = new Pane(new Label("test"));

        Group root = new Group(testPane);

        Scene mainScene = new Scene(root, JavaFxApp.APP_BASE_WIDTH, JavaFxApp.APP_BASE_HEIGHT, JavaFxApp.APP_BACKGROUND_COLOR);

        stage.setTitle(JavaFxApp.APP_NAME);
        stage.setScene(mainScene);
        stage.show();
    }
    
}
