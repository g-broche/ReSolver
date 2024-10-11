package com.gregorybroche.imageresolver.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
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
        void onFormSubmit(ImageTemplate submittedTemplate);
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
        initializeInputValidationMap();
        selectFormat.getItems().addAll("png", "jpg", "bmp", "gif");
        selectFormat.setValue("png");
    }

    @FXML
    void validateTemplateNameChange() {
        String input = validatorService.sanitizeString(inputTemplateName.getText());
        ValidationResponse validationState = templateFormValidatorService.validateTemplateNameInput(input);
        if (!validationState.getIsSuccess()) {
            showInputError(inputTemplateNameError, validationState.getMessage());
        } else {
            hideInputError(inputTemplateNameError);
        }
        setInputValidationForTemplateName(validationState.getIsSuccess());
        toggleSaveButton(areInputsValid());
    }

    @FXML
    void validateImageBaseNameChange() {
        String input = validatorService.sanitizeString(inputFileBaseName.getText());
        ValidationResponse validationState = templateFormValidatorService.validateBaseNameInput(input);
        if (!validationState.getIsSuccess()) {
            showInputError(inputFileBaseNameError, validationState.getMessage());
        } else {
            hideInputError(inputFileBaseNameError);
        }
        setInputValidationForBaseName(validationState.getIsSuccess());
        toggleSaveButton(areInputsValid());
    }

    @FXML
    void validateImagePrefixChange() {
        String input = validatorService.sanitizeString(inputFilePrefix.getText());
        ValidationResponse validationState = templateFormValidatorService.validatePrefixInput(input);
        if (!validationState.getIsSuccess()) {
            showInputError(inputFilePrefixError, validationState.getMessage());
        } else {
            hideInputError(inputFilePrefixError);
        }
        setInputValidationForPrefix(validationState.getIsSuccess());
        toggleSaveButton(areInputsValid());
    }

    @FXML
    void validateImageSuffixChange() {
        String input = validatorService.sanitizeString(inputFileSuffix.getText());
        ValidationResponse validationState = templateFormValidatorService.validateSuffixInput(input);
        if (!validationState.getIsSuccess()) {
            showInputError(inputFileSuffixError, validationState.getMessage());
        } else {
            hideInputError(inputFileSuffixError);
        }
        setInputValidationForSuffix(validationState.getIsSuccess());
        toggleSaveButton(areInputsValid());
    }

    @FXML
    void validateWidthChange() {
        String input = validatorService.sanitizeString(inputWidth.getText());
        ValidationResponse validationState = templateFormValidatorService.validateWidthInput(input);
        if (!validationState.getIsSuccess()) {
            showInputError(inputWidthError, validationState.getMessage());
        } else {
            hideInputError(inputWidthError);
        }
        setInputValidationForWidth(validationState.getIsSuccess());
        toggleSaveButton(areInputsValid());
    }

    @FXML
    void validateHeightChange() {
        String input = validatorService.sanitizeString(inputHeight.getText());
        ValidationResponse validationState = templateFormValidatorService.validateHeightInput(input);
        if (!validationState.getIsSuccess()) {
            showInputError(inputHeightError, validationState.getMessage());
        } else {
            hideInputError(inputHeightError);
        }
        setInputValidationForHeight(validationState.getIsSuccess());
        toggleSaveButton(areInputsValid());
    }

    @FXML
    void validateResolutionChange() {
        String input = validatorService.sanitizeString(inputResolution.getText());
        ValidationResponse validationState = templateFormValidatorService.validateResolutionInput(input);
        if (!validationState.getIsSuccess()) {
            showInputError(inputResolutionError, validationState.getMessage());
        } else {
            hideInputError(inputResolutionError);
        }
        setInputValidationForResolution(validationState.getIsSuccess());
        toggleSaveButton(areInputsValid());
    }

    @FXML
    void validateFormatChange() {
        String input = validatorService.sanitizeString(selectFormat.getValue());
        System.out.println(input);
        ValidationResponse validationState = templateFormValidatorService.validateFormatInput(input);
        if (!validationState.getIsSuccess()) {
            showInputError(selectFormatError, validationState.getMessage());
        } else {
            hideInputError(selectFormatError);
        }
        setInputValidationForFormat(validationState.getIsSuccess());
        toggleSaveButton(areInputsValid());
    }

    public void showInputError(Text textElement, String text) {
        textElement.setText(text);
        textElement.setVisible(true);
    }

    public void hideInputError(Text textElement) {
        textElement.setVisible(false);
        textElement.setText("");
    }

    public void initializeInputValidationMap() {
        setInputValidationForTemplateName(false);
        setInputValidationForBaseName(false);
        setInputValidationForPrefix(false);
        setInputValidationForSuffix(false);
        setInputValidationForWidth(false);
        setInputValidationForHeight(false);
        setInputValidationForResolution(false);
        setInputValidationForFormat(false);
    }

    public void toggleSaveButton(boolean mustEnableButton) {
        buttonSave.setDisable(!mustEnableButton);
    }

    public boolean areInputsValid() {
        return (inputValidationMap.get("templateName")
        && inputValidationMap.get("baseName")
        && inputValidationMap.get("prefix")
        && inputValidationMap.get("suffix")
        && inputValidationMap.get("width")
        && inputValidationMap.get("height")
        && inputValidationMap.get("resolution")
        && inputValidationMap.get("format")
        );
    }

    public void setInputValidationForTemplateName(boolean isInputValid) {
        inputValidationMap.put("templateName", isInputValid);
    }

    public void setInputValidationForBaseName(boolean isInputValid) {
        inputValidationMap.put("baseName", isInputValid);
    }

    public void setInputValidationForPrefix(boolean isInputValid) {
        inputValidationMap.put("prefix", isInputValid);
    }

    public void setInputValidationForSuffix(boolean isInputValid) {
        inputValidationMap.put("suffix", isInputValid);
    }

    public void setInputValidationForWidth(boolean isInputValid) {
        inputValidationMap.put("width", isInputValid);
    }

    public void setInputValidationForHeight(boolean isInputValid) {
        inputValidationMap.put("height", isInputValid);
    }

    public void setInputValidationForResolution(boolean isInputValid) {
        inputValidationMap.put("resolution", isInputValid);
    }

    public void setInputValidationForFormat(boolean isInputValid) {
        inputValidationMap.put("format", isInputValid);
    }
}
