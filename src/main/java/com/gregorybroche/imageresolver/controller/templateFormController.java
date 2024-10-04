package com.gregorybroche.imageresolver.controller;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

@Component
public class templateFormController {
    private TemplateFormSubmitListener submitListener;

    public void setFormSubmitListener(TemplateFormSubmitListener listener) {
        this.submitListener = listener;
    }
    public interface TemplateFormSubmitListener {
        void onFormSubmit(String name, String email);
    }
    
    @FXML
    TextField InputTemplateName;

    @FXML
    TextField InputFileBaseName;

    @FXML
    TextField InputFilePrefix;

    @FXML
    TextField InputFileSuffix;

    @FXML
    CheckBox CheckboxMaintainRatio;

    @FXML
    TextField inputWidth;

    @FXML
    TextField inputHeight;

    @FXML
    TextField inputResolution;

    @FXML
    ChoiceBox selectFormat;

    @FXML
    Button buttonCancel;

    @FXML
    Button buttonSave;
}
