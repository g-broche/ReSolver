package com.gregorybroche.imageresolver.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.interfaces.TemplateDeleteActionListener;
import com.gregorybroche.imageresolver.interfaces.TemplateEditFormSubmitListener;
import com.gregorybroche.imageresolver.service.PresetManagementService;
import com.gregorybroche.imageresolver.service.TemplateFormService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@Component
@Scope("prototype")
public class TemplateItemController {
    private TemplateFormService templateFormService;
    private TemplateEditFormSubmitListener editActionListener;
    private TemplateDeleteActionListener deleteActionListener;
    private int templateIndex;
    private ImageTemplate template;

    public TemplateItemController(TemplateFormService templateFormService, PresetManagementService presetManagementService) {
        this.templateFormService = templateFormService;
    }

    @FXML
    private Pane templateDescriptors;

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
     * opens a modal window to edit the template associated with this controller
     * @param event
     */
    void openEditTemplateModal(ActionEvent event) {
        try {
            Stage stage = templateFormService.createTemplateForm(
                template,
                templateIndex,
                (submittedTemplate, indexInTemplateList) -> {
                    editActionListener.onFormSubmit(submittedTemplate, indexInTemplateList);
                }
            );

            stage.initOwner(((Node) event.getSource()).getScene().getWindow());

            stage.showAndWait();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void setEmitEditListener(TemplateEditFormSubmitListener listener) {
        this.editActionListener = listener;
    }
    public void setEmitDeleteListener(TemplateDeleteActionListener listener) {
        this.deleteActionListener = listener;
    }

    @FXML
    /**
     * call set listener for the delete action
     */
    void deleteTemplate() {
        if(this.deleteActionListener == null){
            return;
        }
        this.deleteActionListener.onDeleteAction(templateIndex);
    }

    /**
     * sets this controller's template data using a template instance and an index
     * 
     * @param imageTemplate
     * @param indexInPreset index of the template inside a preset's template list
     */
    public void setTemplateData(ImageTemplate imageTemplate, int indexInPreset) {
        this.templateIndex = indexInPreset;
        this.template = imageTemplate;
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
