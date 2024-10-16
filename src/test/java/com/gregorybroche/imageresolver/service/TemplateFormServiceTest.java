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

public class TemplateFormServiceTest {
    private TemplateFormService templateFormService;

    @BeforeEach
    void setUp(){
        ValidatorService validatorService = new ValidatorService();
        UserDialogService userDialogService = new UserDialogService(validatorService);
        templateFormService = new TemplateFormService(null, validatorService, userDialogService);
    }

    @Test
    void generateTemplateNameConstraints_shouldReturnAdequateConstraints() {
        try {
            Method generateTemplateNameConstraintsMethod = TemplateFormService.class.getDeclaredMethod("generateTemplateNameConstraints");
            generateTemplateNameConstraintsMethod.setAccessible(true);
            InputConstraint[] templateNameConstraints = (InputConstraint[]) generateTemplateNameConstraintsMethod.invoke(this.templateFormService);
            assertEquals(templateNameConstraints[0].getConstraintName(), "requiredTemplateName");
        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        }
    }

    @Test
    void setAllConstraints_templateFormServiceFormConstraints_shouldContainAllConstraintsNames() {
        this.templateFormService.setAllConstraints();
        InputConstraint[] templateNameConstraints = templateFormService.getFormConstraints().get("templateName");
        assertEquals(templateNameConstraints[0].getConstraintName(), "requiredTemplateName");
        InputConstraint[] widthConstraints = templateFormService.getFormConstraints().get("width");
        assertEquals(widthConstraints[0].getConstraintName(), "requiredWidth");
        InputConstraint[] heightConstraints = templateFormService.getFormConstraints().get("height");
        assertEquals(heightConstraints[0].getConstraintName(), "requiredHeight");
        InputConstraint[] resolutionConstraints = templateFormService.getFormConstraints().get("resolution");
        assertEquals(resolutionConstraints[0].getConstraintName(), "requiredResolution");
        InputConstraint[] prefixConstraints = templateFormService.getFormConstraints().get("prefix");
        assertEquals(prefixConstraints[0].getConstraintName(), "requiredImagePrefix");
        InputConstraint[] formatConstraints = templateFormService.getFormConstraints().get("format");
        assertEquals(formatConstraints[0].getConstraintName(), "requiredFormat");
    }

    @Test
    void validateInput_widthIsNull_shouldReturnValidationResponseFalseWithErrorWidthIsRequired(){
        this.templateFormService.setAllConstraints();
        ValidationResponse testResponse = this.templateFormService.validateInput(null, "width");
        assertFalse(testResponse.getIsSuccess());
        assertEquals(testResponse.getMessage(), "The width is required");
    }

    @Test
    void validateInput_widthIsLessThanAllowed_shouldReturnValidationResponseFalseWithErrorWidthLessThanAllowed(){
        this.templateFormService.setAllConstraints();
        String value = "5";
        String expectedMessage = "The width must be an int greater than";
        ValidationResponse testResponse = this.templateFormService.validateInput(value, "width");
        assertFalse(testResponse.getIsSuccess());
        assertTrue(testResponse.getMessage().contains(expectedMessage));
    }

    @Test
    void validateInput_widthIsMoreThanAllowed_shouldReturnValidationResponseFalseWithErrorWidthGreaterThanAllowed(){
        this.templateFormService.setAllConstraints();
        String value = "11000";
        String expectedMessage = "The width must be an int less than";
        ValidationResponse testResponse = this.templateFormService.validateInput(value, "width");
        assertFalse(testResponse.getIsSuccess());
        assertTrue(testResponse.getMessage().contains(expectedMessage));
    }
}
