package com.gregorybroche.imageresolver.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

@Component
public class StageReadyEventListener implements ApplicationListener<StageReadyEvent>{
    private final ApplicationContext APP_CONTEXT;
    private final String APP_NAME;
    private final int APP_BASE_WIDTH = 1080;
    private final int APP_BASE_HEIGHT = 720;
    private final Color APP_BACKGROUND_COLOR = Color.valueOf("#193947");

    public StageReadyEventListener(ApplicationContext applicationContext, @Value("${app.title}") String applicationName){
        this.APP_NAME = applicationName;
        this.APP_CONTEXT = applicationContext;
    }

    @Override
    public void onApplicationEvent(@NonNull StageReadyEvent event){
        Stage stage = event.getStage();
        Pane testPane = new Pane(new Label("test"));

        Group root = new Group(testPane);

        Scene mainScene = new Scene(root, this.APP_BASE_WIDTH, this.APP_BASE_HEIGHT, this.APP_BACKGROUND_COLOR);

        stage.setTitle(this.APP_NAME);
        stage.setScene(mainScene);
        stage.show();
    }
}
