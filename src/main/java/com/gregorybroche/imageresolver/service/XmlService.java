package com.gregorybroche.imageresolver.service;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
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
    /**
     * creates and XML document which content is based on a given list of presets
     * @param presets
     * @return Document instance for an XML document
     * @throws ParserConfigurationException
     */
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

    /**
     * creates an element instance with its content representing the data of a given preset
     * @param document document instance used to create new nodes
     * @param preset
     * @return Element node with the preset data as sub nodes, or null if missing arguments
     */
    private Element createPresetNode(Document document, Preset preset){
        if (document == null || preset == null) {
            return null;
        }
        Element presetNode = document.createElement("preset");
        Element presetName = document.createElement("name");
        presetName.setTextContent(preset.getName());
        presetNode.appendChild(presetName);
        Element templates = document.createElement("templates");
        for (ImageTemplate template : preset.getTemplates()) {
            Element templateNode = createTemplateNode(document, template);
            templates.appendChild(templateNode);
        }
        presetNode.appendChild(templates);
        return presetNode;
    }

    /**
     * creates an element instance with its content representing the data of a given template
     * @param document document instance used to create new nodes
     * @param template instance of ImageTemplate
     * @return Element node with the template data as sub nodes, or null if missing arguments
     */
    private Element createTemplateNode(Document document, ImageTemplate template){
        if (template == null){
            return null;
        }
        Element templateNode = document.createElement("template");

        Element nameNode = createNode(document, "name", template.getTemplateName());
        templateNode.appendChild(nameNode);
        Element widthNode = createNode(document, "width", Integer.toString(template.getWidth()));
        templateNode.appendChild(widthNode);
        Element heightNode = createNode(document, "height", Integer.toString(template.getHeight()));
        templateNode.appendChild(heightNode);
        Element resolutionNode = createNode(document, "resolution", Integer.toString(template.getResolution()));
        templateNode.appendChild(resolutionNode);
        Element prefixNode = createNode(document, "prefix", template.getNewImagePrefix());
        templateNode.appendChild(prefixNode);
        Element baseNameNode = createNode(document, "baseName", template.getNewImageBaseName());
        templateNode.appendChild(baseNameNode);
        Element suffixNode = createNode(document, "suffix", template.getNewImageSuffix());
        templateNode.appendChild(suffixNode);
        Element formatNode = createNode(document, "format", template.getFormat());
        templateNode.appendChild(formatNode);

        return templateNode;
    }

    /**
     * create document node
     * @param document
     * @param nodeName
     * @return
     */
    private Element createNode(Document document, String nodeName){
        if(document == null || nodeName == null || nodeName.isEmpty()){
            return null;
        }
        Element newNode = document.createElement(nodeName);
        return newNode;
    }

    /**
     * create document node with added text content
     * @param document
     * @param nodeName
     * @param textContent
     * @return
     */
    private Element createNode(Document document, String nodeName, String textContent){
        if(document == null || nodeName == null || nodeName.isEmpty()){
            return null;
        }
        Element newNode = document.createElement(nodeName);
        if(textContent != null && !textContent.isEmpty()){
            newNode.setTextContent(textContent);
        }
        return newNode;
    }


    /**
     * write xml file at a given location
     * @param xmlDocument
     * @param outputPath
     * @return ValidationResponse instance giving the state of success and eventual error messages
     */
    public ValidationResponse writeXML(Document xmlDocument, Path outputPath) {
        try (FileOutputStream output = new FileOutputStream(outputPath.toString())){
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
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
