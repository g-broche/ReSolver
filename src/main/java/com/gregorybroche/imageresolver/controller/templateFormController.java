package com.gregorybroche.imageresolver.controller;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gregorybroche.imageresolver.service.TemplateFormValidatorService;
import com.gregorybroche.imageresolver.service.ValidatorService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

@Component
public class TemplateFormController {
    private TemplateFormSubmitListener submitListener;

    @Autowired
    private TemplateFormValidatorService templateFormValidatorService;
    
    public void setFormSubmitListener(TemplateFormSubmitListener listener) {
        this.submitListener = listener;

    }
    public interface TemplateFormSubmitListener {
        void onFormSubmit();
    }
    
    @FXML
    TextField inputTemplateName;

    @FXML
    Text inputTemplateNameError;

    @FXML
    TextField inputFileBaseName;

    @FXML
    Text inputFileBaseNameError;

    @FXML
    TextField inputFilePrefix;
    
    @FXML
    Text inputFilePrefixError;

    @FXML
    TextField inputFileSuffix;
    
    @FXML
    Text inputFileSuffixError;

    @FXML
    CheckBox checkboxMaintainRatio;

    @FXML
    TextField inputWidth;
    
    @FXML
    Text inputWidthError;

    @FXML
    TextField inputHeight;
    
    @FXML
    Text inputHeightError;

    @FXML
    TextField inputResolution;
    
    @FXML
    Text inputResolutionError;

    @FXML
    ChoiceBox<String> selectFormat;
    
    @FXML
    Text selectFormatError;

    @FXML
    Button buttonCancel;

    @FXML
    Button buttonSave;

    @FXML
    public void initialize() {
    // Populate ChoiceBox with some items
    selectFormat.getItems().addAll("PNG", "JPG", "BMP", "GIF");
    selectFormat.setValue("PNG"); // Set default value if needed
}


    @FXML
    void validateTemplateNameChange() {
        String templateName = inputTemplateName.getText();
        System.out.println(templateName);
    }

    @FXML
    void validateImageBaseNameChange() {
    }

    @FXML
    void validateImagePrefixChange() {
    }

    @FXML
    void validateImageSuffixChange() {
    }

    @FXML
    void validateWidthChange() {
    }

    @FXML
    void validateHeightChange() {
    }

    @FXML
    void validateResolutionChange() {
    }

    @FXML
    void validateFormatChange() {
    }
}
