package com.gregorybroche.imageresolver;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.gregorybroche.imageresolver.event.StageReadyEvent;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class JavaFxApp extends Application{
    private ConfigurableApplicationContext context;

    @Override
    public void init(){
        ApplicationContextInitializer<GenericApplicationContext> initializer =
            context -> {
                context.registerBean(Application.class, () -> JavaFxApp.this);
                context.registerBean(Parameters.class, this::getParameters);
            };
        this.context = new SpringApplicationBuilder()
            .sources(ImageresolverApplication.class)
            .initializers(initializer)
            .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage stage) throws Exception{
        this.context.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        this.context.close();
        Platform.exit();
    }
    
}
