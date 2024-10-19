package com.gregorybroche.imageresolver.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.gregorybroche.imageresolver.Enums.ConstraintType;
import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.classes.InputConstraint;
import com.gregorybroche.imageresolver.classes.ValidationResponse;
import com.gregorybroche.imageresolver.controller.TemplateFormController;
import com.gregorybroche.imageresolver.interfaces.TemplateAddFormSubmitListener;
import com.gregorybroche.imageresolver.interfaces.TemplateEditFormSubmitListener;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Service
public class TemplateFormService {
    private ApplicationContext applicationContext;
    private ValidatorService validatorService;
    private UserDialogService userDialogService;

    private boolean isTemplateNameRequired = true;
    private int minLengthTemplateName = 1;
    private int maxLengthTemplateName = 20;

    private boolean isWidthRequired = true;
    private int minWidth = 16;
    private int maxWidth = 10000;

    private boolean isHeightRequired = true;
    private int minHeight = 16;
    private int maxHeight = 10000;

    private boolean isResolutionRequired = true;
    private int minResolution = 70;
    private int maxResolution = 300;

    private boolean isImagePrefixRequired = false;
    private int minLengthImagePrefix = 1;
    private int maxLengthImagePrefix = 16;

    private boolean isImageSuffixRequired = false;
    private int minLengthImageSuffix = 1;
    private int maxLengthImageSuffix = 16;

    private boolean isImageBaseNameRequired = true;
    private int minLengthImageBaseName = 3;
    private int maxLengthImageBaseName = 50;

    private boolean isFormatRequired = true;
    private String[] allowedFormats;

    private final Map<String, InputConstraint[]> formConstraints = new HashMap<String, InputConstraint[]>();

    public TemplateFormService(
            ApplicationContext applicationContext,
            ValidatorService validatorService,
            UserDialogService userDialogService) {
        this.applicationContext = applicationContext;
        this.validatorService = validatorService;
        this.userDialogService = userDialogService;
        Set<String> allowedFormatsSet = validatorService.getAllowedImageFormatsAsExtension();
        this.allowedFormats = allowedFormatsSet.toArray(new String[allowedFormatsSet.size()]);
        setAllConstraints();
    }

    public Map<String, InputConstraint[]> getFormConstraints() {
        return this.formConstraints;
    }

    public String[] getAllowedFormats() {
        return this.allowedFormats;
    }

    public boolean isTemplateNameRequired() {
        return this.isTemplateNameRequired;
    }

    public boolean isWidthRequired() {
        return this.isWidthRequired;
    }

    public boolean isHeightRequired() {
        return this.isHeightRequired;
    }

    public boolean isResolutionRequired() {
        return this.isResolutionRequired;
    }

    public boolean isImagePrefixRequired() {
        return this.isImagePrefixRequired;
    }

    public boolean isImageSuffixRequired() {
        return this.isImageSuffixRequired;
    }

    public boolean isImageBaseNameRequired() {
        return this.isImageBaseNameRequired;
    }

    public boolean isFormatRequired() {
        return this.isFormatRequired;
    }

    /**
     * Method responsible for creating the stage of the modal window for adding a
     * new template
     * 
     * @param callBackAction action to trigger on form submission based on defined
     *                       interface
     * @return stage of the modal window
     * @throws IOException
     */
    public Stage createTemplateForm(TemplateAddFormSubmitListener callBackAction) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/templateForm.fxml"));
        loader.setControllerFactory(applicationContext::getBean);
        Parent root = loader.load();

        TemplateFormController templateFormController = loader.getController();
        templateFormController.setAddFormSubmitListener(submittedTemplate -> {
            callBackAction.onFormSubmit(submittedTemplate);
        });

        Stage stage = new Stage();
        stage.setTitle("Add Template Form");

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.initModality(Modality.WINDOW_MODAL);

        return stage;
    }

    /**
     * Method responsible for creating the stage of the modal window for editing an
     * existing template
     * 
     * @param templateToEdit  action to trigger on form submission based on defined
     *                        interface
     * @param indexOfTemplate index of template based on the preset it belongs to
     * @param callBackAction  action to trigger on form submission based on defined
     *                        interface
     * @return stage of the modal window
     * @throws IOException
     */
    public Stage createTemplateForm(ImageTemplate templateToEdit, int indexOfTemplate,
            TemplateEditFormSubmitListener callBackAction) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/templateForm.fxml"));
        loader.setControllerFactory(applicationContext::getBean);
        Parent root = loader.load();

        TemplateFormController templateFormController = loader.getController();
        templateFormController.setTemplateToEdit(templateToEdit, indexOfTemplate);
        templateFormController.setEditFormSubmitListener((submittedTemplate, templateIndex) -> {
            callBackAction.onFormSubmit(submittedTemplate, templateIndex);
        });

        Stage stage = new Stage();
        stage.setTitle("Edit Template " + templateToEdit.getTemplateName());

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.initModality(Modality.WINDOW_MODAL);

        return stage;
    }

    /**
     * Set constraints required for all inputs
     */
    public void setAllConstraints() {
        addInputConstraints("templateName", generateTemplateNameConstraints());
        addInputConstraints("width", generateWidthConstraints());
        addInputConstraints("height", generateHeightConstraints());
        addInputConstraints("resolution", generateResolutionConstraints());
        addInputConstraints("prefix", generateImagePrefixConstraints());
        addInputConstraints("baseName", generateImageBaseNameConstraints());
        addInputConstraints("suffix", generateImageSuffixConstraints());
        addInputConstraints("format", generateFormatConstraints());
    }

    public ValidationResponse validateTemplateNameInput(String input) {
        return validateInput(input, "templateName");
    }

    public ValidationResponse validateWidthInput(String input) {
        return validateInput(input, "width");
    }

    public ValidationResponse validateHeightInput(String input) {
        return validateInput(input, "height");
    }

    public ValidationResponse validateResolutionInput(String input) {
        return validateInput(input, "resolution");
    }

    public ValidationResponse validatePrefixInput(String input) {
        return validateInput(input, "prefix");
    }

    public ValidationResponse validateBaseNameInput(String input) {
        return validateInput(input, "baseName");
    }

    public ValidationResponse validateSuffixInput(String input) {
        return validateInput(input, "suffix");
    }

    public ValidationResponse validateFormatInput(String input) {
        return validateInput(input, "format");
    }

    /**
     * Takes in a value to compare it to a set of constraints defined my a
     * constraint key and returns an instance of
     * ValidationResponse with properties isSuccess, data and message
     * 
     * @param input
     * @param inputConstraintKey key correspondind to the constraints defined in
     *                           setAllConstraints
     * @return if a constraint check failed return will have isSuccess at false and
     *         the message corresponding to the failed constraint error message,
     *         otherwise isSuccess will be true and message is null
     */
    public ValidationResponse validateInput(String input, String inputConstraintKey) {
        try {
            InputConstraint[] inputConstraints = this.formConstraints.get(inputConstraintKey);
            for (InputConstraint inputConstraint : inputConstraints) {
                ValidationResponse inputContraintResponse = this.validatorService.isConstraintValidated(input,
                        inputConstraint);
                if (!inputContraintResponse.getIsSuccess()) {
                    return inputContraintResponse;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ValidationResponse(true, null, null);
    }

    /**
     * Adds all constraints relative to a specific input
     * 
     * @param inputKey    key reference for the map collection <String,
     *                    InputConstraint[]> formConstraints and corresponding to
     *                    the input
     * @param constraints array of InputConstraint
     */
    private void addInputConstraints(String inputKey, InputConstraint[] constraints) {
        this.formConstraints.put(inputKey, constraints);
    }

    private InputConstraint[] generateTemplateNameConstraints() {
        List<InputConstraint> templateNameConstraints = new ArrayList<InputConstraint>();
        templateNameConstraints.add(new InputConstraint(
                "requiredTemplateName",
                ConstraintType.REQUIRED,
                this.isTemplateNameRequired,
                "The template name is required"));
        templateNameConstraints.add(new InputConstraint(
                "minimumTemplateName",
                ConstraintType.LONGER_THAN,
                this.minLengthTemplateName,
                "The template name must be longer than " + this.minLengthTemplateName + " characters"));
        templateNameConstraints.add(new InputConstraint(
                "maximumTemplateName",
                ConstraintType.SHORTER_THAN,
                this.maxLengthTemplateName,
                "The template name must be short than " + this.maxLengthTemplateName + " characters"));
        return templateNameConstraints.toArray(new InputConstraint[templateNameConstraints.size()]);
    }

    private InputConstraint[] generateWidthConstraints() {
        List<InputConstraint> templateWidthConstraints = new ArrayList<InputConstraint>();
        templateWidthConstraints.add(new InputConstraint(
                "requiredWidth",
                ConstraintType.REQUIRED,
                this.isWidthRequired,
                "The width is required"));
        templateWidthConstraints.add(new InputConstraint(
                "widthMustBeInteger",
                ConstraintType.IS_INTEGER_STRING,
                true,
                "The width must be a valid integer"));
        templateWidthConstraints.add(new InputConstraint(
                "minimumWidth",
                ConstraintType.GREATER_THAN,
                this.minWidth,
                "The width must be an int greater than " + this.minWidth));
        templateWidthConstraints.add(new InputConstraint(
                "maximumWidth",
                ConstraintType.LESS_THAN,
                this.maxWidth,
                "The width must be an int less than " + this.maxWidth));
        return templateWidthConstraints.toArray(new InputConstraint[templateWidthConstraints.size()]);
    }

    private InputConstraint[] generateHeightConstraints() {
        List<InputConstraint> templateHeightConstraints = new ArrayList<InputConstraint>();
        templateHeightConstraints.add(new InputConstraint(
                "requiredHeight",
                ConstraintType.REQUIRED,
                this.isResolutionRequired,
                "The height is required"));
        templateHeightConstraints.add(new InputConstraint(
                "heightMustBeInteger",
                ConstraintType.IS_INTEGER_STRING,
                true,
                "The height must be a valid integer"));
        templateHeightConstraints.add(new InputConstraint(
                "minimumHeight",
                ConstraintType.GREATER_THAN,
                this.minHeight,
                "The height must be an int greater than " + this.minHeight));
        templateHeightConstraints.add(new InputConstraint(
                "maximumHeight",
                ConstraintType.LESS_THAN,
                this.maxHeight,
                "The height must be an int less than " + this.maxHeight));
        return templateHeightConstraints.toArray(new InputConstraint[templateHeightConstraints.size()]);
    }

    private InputConstraint[] generateResolutionConstraints() {
        List<InputConstraint> templateResolutionConstraints = new ArrayList<InputConstraint>();
        templateResolutionConstraints.add(new InputConstraint(
                "requiredResolution",
                ConstraintType.REQUIRED,
                this.isHeightRequired,
                "The resolution is required"));
        templateResolutionConstraints.add(new InputConstraint(
                "minimumResolution",
                ConstraintType.GREATER_THAN,
                this.minResolution,
                "The resolution must be an int greater than " + this.minResolution));
        templateResolutionConstraints.add(new InputConstraint(
                "maximumResolution",
                ConstraintType.LESS_THAN,
                this.maxResolution,
                "The resolution must be an int less than " + this.maxResolution));
        return templateResolutionConstraints.toArray(new InputConstraint[templateResolutionConstraints.size()]);
    }

    private InputConstraint[] generateImagePrefixConstraints() {
        List<InputConstraint> templateImagePrefixConstraints = new ArrayList<InputConstraint>();
        templateImagePrefixConstraints.add(new InputConstraint(
                "requiredImagePrefix",
                ConstraintType.REQUIRED,
                this.isImagePrefixRequired,
                "The image name prefix is required"));
        templateImagePrefixConstraints.add(new InputConstraint(
                "minimumPrefixLength",
                ConstraintType.LONGER_THAN,
                this.minLengthImagePrefix,
                "The prefix must be longer than " + this.minLengthImagePrefix + " characters"));
        templateImagePrefixConstraints.add(new InputConstraint(
                "maximumPrefixLength",
                ConstraintType.SHORTER_THAN,
                this.maxLengthImagePrefix,
                "The prefix must be shorter than " + this.minLengthImagePrefix + " characters"));
        return templateImagePrefixConstraints
                .toArray(new InputConstraint[templateImagePrefixConstraints.size()]);
    }

    private InputConstraint[] generateImageBaseNameConstraints() {
        List<InputConstraint> templateImageBaseNameConstraints = new ArrayList<InputConstraint>();
        templateImageBaseNameConstraints.add(new InputConstraint(
                "requiredImageBaseName",
                ConstraintType.REQUIRED,
                this.isImageBaseNameRequired,
                "The image base name is required"));
        templateImageBaseNameConstraints.add(new InputConstraint(
                "minimumBaseNameLength",
                ConstraintType.LONGER_THAN,
                this.minLengthImageBaseName,
                "The image base name must be longer than " + this.minLengthImageBaseName
                        + " characters"));
        templateImageBaseNameConstraints.add(new InputConstraint(
                "maximumPrefixLength",
                ConstraintType.SHORTER_THAN,
                this.maxLengthImageBaseName,
                "The image base name must be shorter than " + this.maxLengthImageBaseName
                        + " characters"));
        return templateImageBaseNameConstraints
                .toArray(new InputConstraint[templateImageBaseNameConstraints.size()]);
    }

    private InputConstraint[] generateImageSuffixConstraints() {
        List<InputConstraint> templateImageSuffixConstraints = new ArrayList<InputConstraint>();
        templateImageSuffixConstraints.add(new InputConstraint(
                "requiredImageSuffix",
                ConstraintType.REQUIRED,
                this.isImageSuffixRequired,
                "The image name suffix is required"));
        templateImageSuffixConstraints.add(new InputConstraint(
                "minimumSuffixLength",
                ConstraintType.LONGER_THAN,
                this.minLengthImageSuffix,
                "The suffix must be longer than " + this.minLengthImageSuffix + " characters"));
        templateImageSuffixConstraints.add(new InputConstraint(
                "maximumSuffixLength",
                ConstraintType.SHORTER_THAN,
                this.maxLengthImageSuffix,
                "The suffix must be shorter than " + this.minLengthImageSuffix + " characters"));
        return templateImageSuffixConstraints
                .toArray(new InputConstraint[templateImageSuffixConstraints.size()]);
    }

    private InputConstraint[] generateFormatConstraints() {
        List<InputConstraint> templateFormatConstraints = new ArrayList<InputConstraint>();
        templateFormatConstraints.add(new InputConstraint(
                "requiredFormat",
                ConstraintType.REQUIRED,
                this.isFormatRequired,
                "The format is required"));
        templateFormatConstraints.add(new InputConstraint(
                "allowedFormat",
                ConstraintType.INCLUDED_IN,
                this.allowedFormats,
                "The format must be one of the allowed formats"));
        return templateFormatConstraints.toArray(new InputConstraint[templateFormatConstraints.size()]);
    }
}
