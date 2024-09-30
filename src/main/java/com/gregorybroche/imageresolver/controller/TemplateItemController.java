package com.gregorybroche.imageresolver.controller;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

@Component
public class TemplateItemController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label sizeLabel;

    @FXML
    private Label formatLabel;

    // Method to set the data in the HBox
    public void setTemplateData(String name, int width, int height, String format) {
        nameLabel.setText(name);
        sizeLabel.setText(width + "x" + height);
        formatLabel.setText(format);
    }
}
