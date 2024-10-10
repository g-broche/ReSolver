package com.gregorybroche.imageresolver.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gregorybroche.imageresolver.classes.ValidationResponse;
import com.gregorybroche.imageresolver.service.TemplateFormValidatorService;
import com.gregorybroche.imageresolver.service.ValidatorService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

@Component
public class TemplateFormController {
    private TemplateFormSubmitListener submitListener;
    private Map<String, Boolean> inputValidationMap = new HashMap<String, Boolean>();

    @Autowired
    private TemplateFormValidatorService templateFormValidatorService;

    @Autowired
    private ValidatorService validatorService;
    
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
    initializeInputValidationMap();
}


    @FXML
    void validateTemplateNameChange() {
        String input = validatorService.sanitizeString(inputTemplateName.getText());
        ValidationResponse validationState = templateFormValidatorService.validateTemplateNameInput(input);
        if(!validationState.getIsSuccess()){
            showInputError(inputTemplateNameError, validationState.getMessage());
        }else{
            hideInputError(inputTemplateNameError);
        }
        setInputValidationForTemplateName(validationState.getIsSuccess());
        toggleSaveButton(areInputsValid());
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

    
    public void showInputError(Text textElement, String text){
        textElement.setText(text);
        textElement.setVisible(true);
    }
    public void hideInputError(Text textElement){
        textElement.setVisible(false);
        textElement.setText("");
    }

    public void initializeInputValidationMap(){
        setInputValidationForTemplateName(false);
        setInputValidationForBaseName(false);
        setInputValidationForPrefix(false);
        setInputValidationForSuffix(false);
        setInputValidationForWidth(false);
        setInputValidationForHeight(false);
        setInputValidationForResolution(false);
        setInputValidationForFormat(false);
    }

    public void toggleSaveButton(boolean mustEnableButton){
        buttonSave.setDisable(!mustEnableButton);
    }

    public boolean areInputsValid(){
        return (
            inputValidationMap.get("templateName")
            // && inputValidationMap.get("baseName")
            // && inputValidationMap.get("prefix")
            // && inputValidationMap.get("suffix")
            // && inputValidationMap.get("width")
            // && inputValidationMap.get("height")
            // && inputValidationMap.get("resolution")
            // && inputValidationMap.get("format")
        );
    }

    public void setInputValidationForTemplateName(boolean isInputValid){
        inputValidationMap.put("templateName", isInputValid);
    }
    public void setInputValidationForBaseName(boolean isInputValid){
        inputValidationMap.put("baseName", isInputValid);
    }
    public void setInputValidationForPrefix(boolean isInputValid){
        inputValidationMap.put("prefix", isInputValid);
    }
    public void setInputValidationForSuffix(boolean isInputValid){
        inputValidationMap.put("suffix", isInputValid);
    }
    public void setInputValidationForWidth(boolean isInputValid){
        inputValidationMap.put("width", isInputValid);
    }
    public void setInputValidationForHeight(boolean isInputValid){
        inputValidationMap.put("height", isInputValid);
    }
    public void setInputValidationForResolution(boolean isInputValid){
        inputValidationMap.put("resolution", isInputValid);
    }
    public void setInputValidationForFormat(boolean isInputValid){
        inputValidationMap.put("format", isInputValid);
    }
}
