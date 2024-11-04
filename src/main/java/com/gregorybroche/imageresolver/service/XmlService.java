package com.gregorybroche.imageresolver.service;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.classes.Preset;
import com.gregorybroche.imageresolver.classes.ValidationResponse;

@Service
public class XmlService {
    public Document createPresetsXMLDocument(List<Preset> presets) throws ParserConfigurationException{
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("presets");
        document.appendChild(root);
        if (presets != null) {
            for (Preset preset : presets) {
                Element presetNode = createPresetNode(document, preset);
                root.appendChild(presetNode);
            }
        }
        return document;
    }

    private Element createPresetNode(Document document, Preset preset){
        Element presetNode = document.createElement("preset");
        Element presetName = document.createElement("name");
        presetName.setTextContent(preset.getName());
        presetNode.appendChild(presetName);
        Element templates = document.createElement("templates");
        for (ImageTemplate template : preset.getTemplates()) {
            
        }
        presetNode.appendChild(templates);
        return presetNode;
    }

    public ValidationResponse writeXML(Document xmlDocument, Path outputPath) {
        try (FileOutputStream output = new FileOutputStream(outputPath.toString())){
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(xmlDocument);
            StreamResult result = new StreamResult(output);
            transformer.transform(source, result);
            return new ValidationResponse(true, null, null);
        } catch (Exception e) {
            String errorMessage = "could not write XML file at location '"+outputPath.toString()+"' ; error : "+e.getMessage();
            return new ValidationResponse(false, null, errorMessage);
        }

    }
}
