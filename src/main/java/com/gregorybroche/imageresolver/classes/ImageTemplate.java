package com.gregorybroche.imageresolver.classes;

import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.gregorybroche.imageresolver.controller.TemplateItemController;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class ImageTemplate {
    private int width = 600;
    private int height = 400;
    private String newImageName = "test-editing";
    private String format = "jpg";

    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() {
        return width;
    }

    public void setNewImageName(String newImageName) {
        this.newImageName = newImageName;
    }
    public String getNewImageName() {
        return newImageName;
    }

    public void setFormat(String format) {
        this.format = format;
    }
    public String getFormat() {
        return format;
    }

    /**
     * Creates and returns the FXML component view with data corresponding to this instance of ImageTemplate
     * @return
     */
    public HBox createTemplatePane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/templateComponent.fxml"));
            HBox templatePane = loader.load();

            TemplateItemController controller = loader.getController();

            controller.setTemplateData(newImageName, width, height, format);

            return templatePane;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
