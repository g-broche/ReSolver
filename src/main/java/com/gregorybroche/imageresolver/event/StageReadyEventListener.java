package com.gregorybroche.imageresolver.event;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class StageReadyEventListener implements ApplicationListener<StageReadyEvent>{
    private final ApplicationContext APP_CONTEXT;
    private final Resource FXML;
    private final Resource CSS;
    private final String APP_NAME = "ReSolver";

    public StageReadyEventListener( ApplicationContext applicationContext,
                                    @Value("classpath:/com/gregorybroche/imageresolver/views/main.fxml") Resource fxml,
                                    @Value("classpath:/com/gregorybroche/imageresolver/style/app.css") Resource css){
        this.APP_CONTEXT = applicationContext;
        this.FXML = fxml;
        this.CSS = css;
    }

    @Override
    public void onApplicationEvent(@NonNull StageReadyEvent event){
        try {
            Stage stage = event.getStage();
            URL viewUrl = this.FXML.getURL();
            FXMLLoader loader = new FXMLLoader(viewUrl);
            loader.setControllerFactory(this.APP_CONTEXT::getBean);
            Parent root = loader.load();
            Scene mainScene = new Scene(root);

            String css = this.CSS.getURL().toExternalForm();
            mainScene.getStylesheets().add(css);

            stage.setTitle(this.APP_NAME);
            stage.setScene(mainScene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
