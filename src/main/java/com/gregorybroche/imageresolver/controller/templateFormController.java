package com.gregorybroche.imageresolver.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.classes.ValidationResponse;
import com.gregorybroche.imageresolver.interfaces.TemplateAddFormSubmitListener;
import com.gregorybroche.imageresolver.interfaces.TemplateEditFormSubmitListener;
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
    private TemplateAddFormSubmitListener submitAddListener = null;
    private TemplateEditFormSubmitListener submitEditListener = null;
    private Map<String, Boolean> inputValidationMap = new HashMap<String, Boolean>();
    private ImageTemplate templateToEdit = null;
    private Integer indexOfTemplateToEdit = null;

    @Autowired
    private TemplateFormService templateFormValidatorService;

    @Autowired
    private ValidatorService validatorService;

    public void setAddFormSubmitListener(TemplateAddFormSubmitListener listener) {
        this.submitAddListener = listener;
    }
    public void setEditFormSubmitListener(TemplateEditFormSubmitListener listener) {
        this.submitEditListener = listener;
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
        String[] allowedOutputFormats = templateFormValidatorService.getAllowedFormats();
        selectFormat.getItems().addAll(allowedOutputFormats);
        selectFormat.setValue(allowedOutputFormats[0]);
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
    /**
     * On form submit will verify if a listener is defined as callback to handle the action and if the inputs are valid
     * if these conditions are true, an ImageTemplate instance is created and passed to the listener that has been set
     * for the submit action (either add or edit template)
     */
    private void handleSubmit() { 
        if(this.submitAddListener == null && this.submitEditListener == null){
            Exception noListenerError = new Exception("No listener set on template form");
            throw new RuntimeException(noListenerError);
        }

        if(!areInputsValid()){
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

            ImageTemplate submittedTemplate = new ImageTemplate(
                templateName,
                width,
                height,
                resolution,
                prefix,
                baseName,
                suffix,
                format);

            if(submitEditListener != null){
                submitEditListener.onFormSubmit(submittedTemplate, indexOfTemplateToEdit);
                closeModal();
                return;
            }
            submitAddListener.onFormSubmit(submittedTemplate);
            closeModal();
            return;
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void cancelForm(){
        closeModal();
    }

    /**
     * set this controllers associated template and its index in case of editing form, also triggers auto filling of fields using the template's data
     * @param templateToEdit
     * @param indexToEdit
     */
    public void setTemplateToEdit(ImageTemplate templateToEdit, int indexToEdit){
        this.templateToEdit = templateToEdit;
        this.indexOfTemplateToEdit = indexToEdit;
        setFormatChoiceBox(templateToEdit);
        setFieldsOnEditForm(templateToEdit);
        validateAllInputs();

    }

    private void validateAllInputs(){
        validateTemplateNameChange();
        validateImageBaseNameChange();
        validateImagePrefixChange();
        validateImageSuffixChange();
        validateWidthChange();
        validateHeightChange();
        validateResolutionChange();
        validateFormatChange();
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

    /**
     * sets the value of all input validation booleans to reflect if the associated values are required by defined template contraints
     */
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

    /**
     * checks if the map of booleans corresponding to validation state of all inputs contains only true values
     * @return
     */
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

    /**
     * fills all form fields using a template instance as data source, used for loading infos of a template to edit
     * @param template
     */
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
        String defaultSelectedValue = template != null && validatorService.isIncludedIn(template.getFormat(), allowedOutputFormats)
            ? template.getFormat()
            : allowedOutputFormats[0];
        selectFormat.setValue(defaultSelectedValue);
    }
}
