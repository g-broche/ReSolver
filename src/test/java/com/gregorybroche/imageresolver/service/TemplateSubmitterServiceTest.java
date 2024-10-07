package com.gregorybroche.imageresolver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gregorybroche.imageresolver.classes.InputConstraint;

public class TemplateSubmitterServiceTest {
    private TemplateSubmitterService templateSubmitterService;

    @BeforeEach
    void setUp(){
        ValidatorService validatorService = new ValidatorService();
        templateSubmitterService = new TemplateSubmitterService(validatorService);
    }

    @Test
    void generateTemplateNameConstraints_shouldReturnAdequateConstraints() {
        try {
            Method generateTemplateNameConstraintsMethod = TemplateSubmitterService.class.getDeclaredMethod("generateTemplateNameConstraints");
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
}
