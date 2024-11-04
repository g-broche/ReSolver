package com.gregorybroche.imageresolver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.classes.InputConstraint;
import com.gregorybroche.imageresolver.classes.Preset;

public class XmlServiceTest {
    private XmlService xmlService;
    private Preset preset1 = new Preset("preset1", new ArrayList<ImageTemplate>());
    private Preset preset2 = new Preset("preset2", new ArrayList<ImageTemplate>());
    private ImageTemplate templateTest1 = new ImageTemplate(
        "templateTest1",
        1920,
        1080,
        96,
        "XL",
        "test-image",
        null,
        "jpg"
        );

    private ImageTemplate templateTest2 = new ImageTemplate(
        "templateTest2",
        1080,
        720,
        96,
        "L",
        "test-image",
        null,
        "jpg"
        );

    private ImageTemplate templateTest3 = new ImageTemplate(
        "templateTest3",
        720,
        480,
        96,
        "M",
        "test-image",
        null,
        "jpg"
        );

    private ImageTemplate templateTest4 = new ImageTemplate(
        "templateTest4",
        360,
        240,
        96,
        "S",
        "test-image",
        null,
        "jpg"
        );

    public XmlServiceTest(){
        this.xmlService = new XmlService();
    }

    @Test
    void createPresetNode_withValidPreset_ShouldReturnCreatedNode(){
        try {
            Method createPresetNodeMethod = XmlService.class.getDeclaredMethod("createPresetNode", Document.class, Preset.class);
            createPresetNodeMethod.setAccessible(true);
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
    
            Object[] args = new Object[]{document, this.preset1};
            Element createdNode = (Element) createPresetNodeMethod.invoke(this.xmlService, args);
            NodeList presetDataNodes = createdNode.getChildNodes();
            Element nameNode = (Element) presetDataNodes.item(0);
            assertEquals(this.preset1.getName(), nameNode.getTextContent());
            assertTrue(true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            fail();
        }    
    }
}
