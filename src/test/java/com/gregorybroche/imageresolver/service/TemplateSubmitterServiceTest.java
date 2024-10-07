package com.gregorybroche.imageresolver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
}
