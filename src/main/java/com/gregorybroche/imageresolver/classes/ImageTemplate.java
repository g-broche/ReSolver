package com.gregorybroche.imageresolver.classes;

import java.io.IOException;

import org.springframework.context.ApplicationContext;

import com.gregorybroche.imageresolver.controller.TemplateItemController;
import com.gregorybroche.imageresolver.interfaces.TemplateEditFormSubmitListener;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class ImageTemplate {
    private String templateName;
    private int width;
    private int height;
    private int resolution;
    private String newImagePrefix;
    private String newImageBaseName;
    private String newImageSuffix;
    private String format;
    private int defaultResolution = 90;

    public ImageTemplate(
        String templateName,
        int width,
        int height,
        Integer resolution,
        String newImagePrefix,
        String newImageBaseName,
        String newImageSuffix,
        String format
        ){
        setTemplateName(templateName);
        setWidth(width);
        setHeight(height);
        setResolution(resolution);
        setNewImagePrefix(newImagePrefix);
        setNewImageBaseName(newImageBaseName);
        setNewImageSuffix(newImageSuffix);
        setFormat(format);
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName.length() > 0 ? templateName : "unnamed template";
    }
    public String getTemplateName() {
        return this.templateName;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight() {
        return this.height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() {
        return this.width;
    }

    public void setResolution(Integer resolution) {
        this.resolution = resolution != null ? resolution : this.defaultResolution;
    }
    public int getResolution() {
        return this.resolution;
    }

    public void setNewImagePrefix(String prefix) {
        this.newImagePrefix = prefix == null || prefix.length() == 0 ? "" : prefix;
    }
    public String getNewImagePrefix() {
        return this.newImagePrefix;
    }

    public void setNewImageBaseName(String baseName) {
        this.newImageBaseName = baseName;
    }
    public String getNewImageBaseName() {
        return this.newImageBaseName;
    }

    public void setNewImageSuffix(String suffix) {
        this.newImageSuffix = suffix == null || suffix.length() == 0 ? "" : suffix;
    }
    public String getNewImageSuffix() {
        return this.newImageSuffix;
    }

    public void setFormat(String format) {
        this.format = format;
    }
    public String getFormat() {
        return this.format;
    }

    public String getCompleteFileName() {
        return this.newImagePrefix+this.newImageBaseName+this.newImageSuffix+this.format;
    }

    /**
     * Creates and returns the FXML component view with data corresponding to this instance of ImageTemplate
     * @return
     */
    public HBox createTemplateComponent(Integer indexInPreset, ApplicationContext applicationContext, TemplateEditFormSubmitListener callbackOnEditAction){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/templateComponent.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            HBox templateHBox = loader.load();
            TemplateItemController controller = loader.getController();
            controller.setTemplateData(this, indexInPreset);
            controller.setEmitEditListener(callbackOnEditAction);
            return templateHBox;

        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
