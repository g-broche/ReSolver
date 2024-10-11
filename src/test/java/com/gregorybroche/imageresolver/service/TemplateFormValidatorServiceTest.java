package com.gregorybroche.imageresolver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gregorybroche.imageresolver.classes.InputConstraint;
import com.gregorybroche.imageresolver.classes.ValidationResponse;

public class TemplateFormValidatorServiceTest {
    private TemplateFormValidatorService templateSubmitterService;

    @BeforeEach
    void setUp(){
        ValidatorService validatorService = new ValidatorService();
        UserDialogService userDialogService = new UserDialogService(validatorService);
        templateSubmitterService = new TemplateFormValidatorService(validatorService, userDialogService);
    }

    @Test
    void generateTemplateNameConstraints_shouldReturnAdequateConstraints() {
        try {
            Method generateTemplateNameConstraintsMethod = TemplateFormValidatorService.class.getDeclaredMethod("generateTemplateNameConstraints");
            generateTemplateNameConstraintsMethod.setAccessible(true);
            InputConstraint[] templateNameConstraints = (InputConstraint[]) generateTemplateNameConstraintsMethod.invoke(this.templateSubmitterService);
            assertEquals(templateNameConstraints[0].getConstraintName(), "requiredTemplateName");
        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        }
    }

    @Test
    void setAllConstraints_TemplateSubmitterServiceFormConstraints_shouldContainAllConstraintsNames() {
        this.templateSubmitterService.setAllConstraints();
        InputConstraint[] templateNameConstraints = templateSubmitterService.getFormConstraints().get("templateName");
        assertEquals(templateNameConstraints[0].getConstraintName(), "requiredTemplateName");
        InputConstraint[] widthConstraints = templateSubmitterService.getFormConstraints().get("width");
        assertEquals(widthConstraints[0].getConstraintName(), "requiredWidth");
        InputConstraint[] heightConstraints = templateSubmitterService.getFormConstraints().get("height");
        assertEquals(heightConstraints[0].getConstraintName(), "requiredHeight");
        InputConstraint[] resolutionConstraints = templateSubmitterService.getFormConstraints().get("resolution");
        assertEquals(resolutionConstraints[0].getConstraintName(), "requiredResolution");
        InputConstraint[] prefixConstraints = templateSubmitterService.getFormConstraints().get("prefix");
        assertEquals(prefixConstraints[0].getConstraintName(), "requiredImagePrefix");
        InputConstraint[] formatConstraints = templateSubmitterService.getFormConstraints().get("format");
        assertEquals(formatConstraints[0].getConstraintName(), "requiredFormat");
    }

    @Test
    void validateInput_widthIsNull_shouldReturnValidationResponseFalseWithErrorWidthIsRequired(){
        this.templateSubmitterService.setAllConstraints();
        ValidationResponse testResponse = this.templateSubmitterService.validateInput(null, "width");
        assertFalse(testResponse.getIsSuccess());
        assertEquals(testResponse.getMessage(), "The width is required");
    }

    @Test
    void validateInput_widthIsLessThanAllowed_shouldReturnValidationResponseFalseWithErrorWidthLessThanAllowed(){
        this.templateSubmitterService.setAllConstraints();
        int value = 5;
        String expectedMessage = "The width must be an int greater than";
        ValidationResponse testResponse = this.templateSubmitterService.validateInput(value, "width");
        assertFalse(testResponse.getIsSuccess());
        assertTrue(testResponse.getMessage().contains(expectedMessage));
    }

    @Test
    void validateInput_widthIsMoreThanAllowed_shouldReturnValidationResponseFalseWithErrorWidthGreaterThanAllowed(){
        this.templateSubmitterService.setAllConstraints();
        int value = 11000;
        String expectedMessage = "The width must be an int less than";
        ValidationResponse testResponse = this.templateSubmitterService.validateInput(value, "width");
        assertFalse(testResponse.getIsSuccess());
        assertTrue(testResponse.getMessage().contains(expectedMessage));
    }
}
