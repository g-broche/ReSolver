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

    private boolean isHeigthRequired = true;
    private int minHeigth = 16;
    private int maxHeigth = 10000;

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

    public Map<String, InputConstraint[]>  getFormConstraints(){
        return this.formConstraints;
    }

    public void setAllConstraints(){
        addInputConstraints("templateName", generateTemplateNameConstraints());
    }

    public void addInputConstraints(String inputKey, InputConstraint[] constraints){
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
                    "The template name must be longer than "+this.minLengthTemplateName+" characters"));
        templateNameConstraints.add(new InputConstraint(
                    "maximumTemplateName",
                    ConstraintType.SHORTER_THAN,
                    this.maxLengthTemplateName,
                    "The template name must be short than "+this.maxLengthTemplateName+" characters"));
        return templateNameConstraints.toArray(new InputConstraint[templateNameConstraints.size()]);
    }
}
