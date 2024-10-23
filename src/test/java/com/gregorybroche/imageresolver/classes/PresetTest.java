package com.gregorybroche.imageresolver.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    void testAddTemplate_addingOneTemplate_shouldHaveOneElementInList() {
        Preset testPreset = new Preset("testPreset", new ArrayList<ImageTemplate>());
        testPreset.addTemplate(template1);
        assertTrue(testPreset.getTemplates().size() == 1, "Should have one element");
        assertTrue(testPreset.getTemplates().get(0).getTemplateName().equals("template1"), "Should retrieve name of added template");
    }

    @Test
    void testAddTemplate_addingThreeSuccessiveTemplate_shouldHaveThreeElementsInList() {
        Preset testPreset = new Preset("testPreset", new ArrayList<ImageTemplate>());
        testPreset.addTemplate(template1);
        testPreset.addTemplate(template2);
        testPreset.addTemplate(template3);
        assertTrue(testPreset.getTemplates().size() == 3, "Should have one element");
        assertTrue(testPreset.getTemplates().get(0).getTemplateName().equals("template1"), "Should retrieve name of first added template");
        assertTrue(testPreset.getTemplates().get(1).getTemplateName().equals("template2"), "Should retrieve name of second added template");
        assertTrue(testPreset.getTemplates().get(2).getTemplateName().equals("template3"), "Should retrieve name of third added template");
    }

    @Test
    void testDeleteTemplate_deleteOutOfBoundIndex_shouldHaveUnchangedSizeAndReturnFalse() {
        Preset testPreset = new Preset("testPreset", new ArrayList<ImageTemplate>());
        testPreset.addTemplate(template1);
        testPreset.addTemplate(template2);
        testPreset.addTemplate(template3);
        boolean result = testPreset.deleteTemplate(3);
        assertEquals(testPreset.getTemplates().size(), 3);
        assertFalse(result);
    }

    @Test
    void testDeleteTemplate_deletevalidIndex_shouldMinusOneSizeAndReturnTrue() {
        Preset testPreset = new Preset("testPreset", new ArrayList<ImageTemplate>());
        testPreset.addTemplate(template1);
        testPreset.addTemplate(template2);
        testPreset.addTemplate(template3);
        boolean result = testPreset.deleteTemplate(1);
        assertEquals(testPreset.getTemplates().size(), 2);
        assertTrue(result);
    }
}
