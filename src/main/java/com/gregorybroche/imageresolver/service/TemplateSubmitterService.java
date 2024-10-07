package com.gregorybroche.imageresolver.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.gregorybroche.imageresolver.Enums.ConstraintType;
import com.gregorybroche.imageresolver.classes.InputConstraint;

@Service
public class TemplateSubmitterService {
    private ValidatorService validatorService;

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
    private int minLengthImageBaseName = 5;
    private int maxLengthImageBaseName = 50;

    private boolean isFormatRequired = true;
    private String[] allowedFormats;

    private final Map<String, InputConstraint[]> formConstraints = new HashMap<String, InputConstraint[]>();

    public TemplateSubmitterService(ValidatorService validatorService) {
        this.validatorService = validatorService;
        Set<String> allowedFormatsSet = validatorService.getAllowedImageFormatsAsExtension();
        this.allowedFormats = allowedFormatsSet.toArray(new String[allowedFormatsSet.size()]);
    }

    public Map<String, InputConstraint[]> getFormConstraints() {
        return this.formConstraints;
    }

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
                ConstraintType.LESS_THAN,
                this.maxLengthImagePrefix,
                "The prefix must be shorter than " + this.minLengthImagePrefix + " characters"));
        return templateImagePrefixConstraints.toArray(new InputConstraint[templateImagePrefixConstraints.size()]);
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
                "The image base name must be longer than " + this.minLengthImageBaseName + " characters"));
                templateImageBaseNameConstraints.add(new InputConstraint(
                "maximumPrefixLength",
                ConstraintType.LESS_THAN,
                this.maxLengthImageBaseName,
                "The image base name must be shorter than " + this.maxLengthImageBaseName + " characters"));
        return templateImageBaseNameConstraints.toArray(new InputConstraint[templateImageBaseNameConstraints.size()]);
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
                ConstraintType.LESS_THAN,
                this.maxLengthImageSuffix,
                "The suffix must be shorter than " + this.minLengthImageSuffix + " characters"));
        return templateImageSuffixConstraints.toArray(new InputConstraint[templateImageSuffixConstraints.size()]);
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