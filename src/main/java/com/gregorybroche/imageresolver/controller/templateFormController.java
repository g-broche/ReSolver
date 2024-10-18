package com.gregorybroche.imageresolver.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.classes.ValidationResponse;
import com.gregorybroche.imageresolver.interfaces.TemplateFormSubmitListener;
import com.gregorybroche.imageresolver.service.TemplateFormService;
import com.gregorybroche.imageresolver.service.ValidatorService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@Component
public class TemplateFormController {
    private TemplateFormSubmitListener submitListener;
    private Map<String, Boolean> inputValidationMap = new HashMap<String, Boolean>();
    private Integer indexOfTemplateToEdit = null;
    private ImageTemplate templateToEdit = null;

    @Autowired
    private TemplateFormService templateFormValidatorService;

    @Autowired
    private ValidatorService validatorService;

    public void setFormSubmitListener(TemplateFormSubmitListener listener) {
        this.submitListener = listener;
    }

    @FXML
    Text templateFormTitleText;

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
    private void initialize() {
        initializeInputValidationMap();
        templateFormTitleText.setText(templateToEdit == null ? "ADD TEMPLATE" : "EDIT TEMPLATE");
        setFormatChoiceBox(templateToEdit);
        if(templateToEdit != null){
            setFieldsOnEditForm(templateToEdit);
        }
    }

    @FXML
    private void validateTemplateNameChange() {
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
    private void validateImageBaseNameChange() {
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
    private void validateImagePrefixChange() {
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
    private void validateImageSuffixChange() {
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
    private void validateWidthChange() {
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
    private void validateHeightChange() {
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
    private void validateResolutionChange() {
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
    private void validateFormatChange() {
        String input = validatorService.sanitizeString(selectFormat.getValue());
        ValidationResponse validationState = templateFormValidatorService.validateFormatInput(input);
        if (!validationState.getIsSuccess()) {
            showInputError(selectFormatError, validationState.getMessage());
        } else {
            hideInputError(selectFormatError);
        }
        setInputValidationForFormat(validationState.getIsSuccess());
        toggleSaveButton(areInputsValid());
    }

    @FXML
    private void handleSubmit() {
        if(!areInputsValid()){
            return;
        }
        if(this.submitListener == null){
            System.err.println("listener not set");
            return;
        }
        try {
            String templateName = validatorService.sanitizeString(inputTemplateName.getText());
            String baseName = validatorService.sanitizeString(inputFileBaseName.getText());
            String prefix = validatorService.sanitizeString(inputFilePrefix.getText());
            String suffix = validatorService.sanitizeString(inputFileSuffix.getText());
            int width = validatorService.sanitizeStringAsInteger(inputWidth.getText());
            int height = validatorService.sanitizeStringAsInteger(inputHeight.getText());
            int resolution = validatorService.sanitizeStringAsInteger(inputResolution.getText());
            String format = validatorService.sanitizeString(selectFormat.getValue());

            ImageTemplate templateToAdd = new ImageTemplate(
                templateName,
                width,
                height,
                resolution,
                prefix,
                baseName,
                suffix,
                format);

            this.submitListener.onFormSubmit(templateToAdd);
            closeModal();
            
        } catch (Exception e) {
            System.err.println("handle submit - catch");
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void cancelForm(){
        closeModal();
    }

    private void closeModal(){
        Stage modalStage = (Stage) buttonSave.getScene().getWindow();
        modalStage.close();
    }

    private void showInputError(Text textElement, String text) {
        textElement.setText(text);
        textElement.setVisible(true);
    }

    private void hideInputError(Text textElement) {
        textElement.setVisible(false);
        textElement.setText("");
    }

    private void initializeInputValidationMap() {
        setInputValidationForTemplateName(!templateFormValidatorService.isTemplateNameRequired());
        setInputValidationForBaseName(!templateFormValidatorService.isImageBaseNameRequired());
        setInputValidationForPrefix(!templateFormValidatorService.isImagePrefixRequired());
        setInputValidationForSuffix(!templateFormValidatorService.isImageSuffixRequired());
        setInputValidationForWidth(!templateFormValidatorService.isWidthRequired());
        setInputValidationForHeight(!templateFormValidatorService.isHeightRequired());
        setInputValidationForResolution(!templateFormValidatorService.isResolutionRequired());
        setInputValidationForFormat(!templateFormValidatorService.isFormatRequired());
    }

    private void toggleSaveButton(boolean mustEnableButton) {
        buttonSave.setDisable(!mustEnableButton);
    }

    private boolean areInputsValid() {
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

    private void setInputValidationForTemplateName(boolean isInputValid) {
        inputValidationMap.put("templateName", isInputValid);
    }

    private void setInputValidationForBaseName(boolean isInputValid) {
        inputValidationMap.put("baseName", isInputValid);
    }

    private void setInputValidationForPrefix(boolean isInputValid) {
        inputValidationMap.put("prefix", isInputValid);
    }

    private void setInputValidationForSuffix(boolean isInputValid) {
        inputValidationMap.put("suffix", isInputValid);
    }

    private void setInputValidationForWidth(boolean isInputValid) {
        inputValidationMap.put("width", isInputValid);
    }

    private void setInputValidationForHeight(boolean isInputValid) {
        inputValidationMap.put("height", isInputValid);
    }

    private void setInputValidationForResolution(boolean isInputValid) {
        inputValidationMap.put("resolution", isInputValid);
    }

    private void setInputValidationForFormat(boolean isInputValid) {
        inputValidationMap.put("format", isInputValid);
    }

    private void setFieldsOnEditForm(ImageTemplate template){
        setTextInputValue(inputTemplateName, template.getTemplateName());
        setTextInputValue(inputFileBaseName, template.getNewImageBaseName());
        setTextInputValue(inputFilePrefix, template.getNewImagePrefix());
        setTextInputValue(inputFileSuffix, template.getNewImageSuffix());
        setTextInputValue(inputWidth, String.valueOf(template.getWidth()));
        setTextInputValue(inputHeight, String.valueOf(template.getHeight()));
        setTextInputValue(inputResolution, String.valueOf(template.getResolution()));
    }

    private void setTextInputValue(TextField inputField, String value){
        if(inputField == null || value == null){
            return;
        }
        inputField.setText(value);
    }

    /**
     * Fills the choice box for format using allowed formats defined in ValidatorService. Will initialize selected value to first format
     * or if there is a template instance given will set the selected value to the template format.
     * @param template
     */
    private void setFormatChoiceBox(ImageTemplate template){
        String[] allowedOutputFormats = templateFormValidatorService.getAllowedFormats();
        selectFormat.getItems().addAll(allowedOutputFormats);
        String defaultSelectedValue = template != null && validatorService.isIncludedIn(template.getFormat(), allowedOutputFormats)
            ? template.getFormat()
            : allowedOutputFormats[0];
        selectFormat.setValue(defaultSelectedValue);
    }
}
