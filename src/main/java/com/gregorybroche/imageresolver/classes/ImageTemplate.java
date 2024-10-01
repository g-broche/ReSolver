package com.gregorybroche.imageresolver.classes;

import java.io.IOException;

import com.gregorybroche.imageresolver.controller.TemplateItemController;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class ImageTemplate {
    private String templateName = "PlaceHolder name";
    private int width = 600;
    private int height = 400;
    private int resolution = 72;
    private String newImagePrefix = "pre-";
    private String newImageBaseName = "test_editing";
    private String newImageSuffix = "-post";
    private String format = "jpg";
    private int defaultResolution = 90;

    public ImageTemplate(String templateName,
                        int width,
                        int height,
                        Integer resolution,
                        String newImagePrefix,
                        String newImageBaseName,
                        String newImageSuffix,
                        String format){
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
        this.newImagePrefix = prefix.length() > 0 ? prefix : "";
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
        this.newImageSuffix = suffix.length() > 0 ? suffix : "";
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
    public HBox createTemplatePane(Integer indexInPreset) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/templateComponent.fxml"));
            HBox templatePane = loader.load();

            TemplateItemController controller = loader.getController();

            controller.setTemplateData(this, indexInPreset);

            return templatePane;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
