package com.gregorybroche.imageresolver.classes;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PresetTest {
    private ImageTemplate template1;
    private ImageTemplate template2;
    private ImageTemplate template3;

    @BeforeEach
    void setUp() {
        this.template1= new ImageTemplate(
            "template1",
            400,
            200,
            72,
            "test-",
            "image1", 
            null, 
            "jpg");

        this.template2= new ImageTemplate(
            "template2",
            600,
            300,
            90,
            "test-",
            "image2", 
            null, 
            "jpg");

        this.template3= new ImageTemplate(
            "template3",
            1920,
            1080,
            72,
            "test-",
            "image3", 
            "-large", 
            "png");
    }


    @Test
    void testAddTemplate_addingOneTemplate_ShouldHaveOneElementInList() {
        Preset testPreset = new Preset("testPreset", new ArrayList<ImageTemplate>());
        testPreset.addTemplate(template1);
        assertTrue(testPreset.getTemplates().size() == 1, "Should have one element");
        assertTrue(testPreset.getTemplates().get(0).getTemplateName().equals("template1"), "Should retrieve name of added template");
    }

    
}
