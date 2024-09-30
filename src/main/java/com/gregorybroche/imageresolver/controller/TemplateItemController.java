package com.gregorybroche.imageresolver.controller;

import org.springframework.stereotype.Component;

import com.gregorybroche.imageresolver.classes.ImageTemplate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

@Component
public class TemplateItemController {
    @FXML
    private Label templateName;

    @FXML
    private Label templateImageFormat;

    @FXML
    private TableView<ImageTemplate> templateNamingTable;

    @FXML
    private TableColumn<ImageTemplate, String> prefixColumn;

    @FXML
    private TableColumn<ImageTemplate, String> baseNameColumn;

    @FXML
    private TableColumn<ImageTemplate, String> suffixColumn;

    @FXML
    private TableView<ImageTemplate> templateSpecsTable;

    @FXML
    private TableColumn<ImageTemplate, Integer> widthColumn;

    @FXML
    private TableColumn<ImageTemplate, Integer> heigthColumn;

    @FXML
    private TableColumn<ImageTemplate, Integer> resolutionColumn;

    @FXML
    private TableColumn<ImageTemplate, String> formatColumn;

    @FXML
    private Button templateEditButton;

    @FXML
    private Button templateDeleteButton;


    @FXML
    /**
     * placeholder for editing this template
     * @param event
     */
    void editTemplate(MouseEvent event) {
        System.out.println("implement edit window action");
    }

    @FXML
    /**
     * placeholder for deleting this template
     * @param event
     */
    void deleteTemplate(MouseEvent event) {
        System.out.println("implement delete action");
    }

    // Method to set the data in the HBox
    public void setTemplateData(ImageTemplate imageTemplate, Integer indexInPreset) {
        setLabels(imageTemplate);
        setFileNamingConvention(imageTemplate);
        setSizeInfo(imageTemplate);
    }

    private void setLabels(ImageTemplate imageTemplate) {
        templateName.setText(imageTemplate.getTemplateName().length() > 0 ? imageTemplate.getTemplateName() : "unnamed");
    }

    private void setFileNamingConvention(ImageTemplate imageTemplate) {
        prefixColumn.setCellValueFactory(new PropertyValueFactory<>("newImagePrefix"));
        baseNameColumn.setCellValueFactory(new PropertyValueFactory<>("newImageBaseName"));
        suffixColumn.setCellValueFactory(new PropertyValueFactory<>("newImageSuffix"));

        ObservableList<ImageTemplate> data = FXCollections.observableArrayList(imageTemplate);
        templateNamingTable.setItems(data);
    }

    private void setSizeInfo(ImageTemplate imageTemplate) {
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));
        heigthColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
        resolutionColumn.setCellValueFactory(new PropertyValueFactory<>("resolution"));
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("format"));

        ObservableList<ImageTemplate> data = FXCollections.observableArrayList(imageTemplate);
        templateSpecsTable.setItems(data);
    }
}
