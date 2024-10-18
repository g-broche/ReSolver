package com.gregorybroche.imageresolver.classes;

import java.util.ArrayList;
import java.util.List;

public class Preset {
    private String name;
    private List<ImageTemplate> templates = new ArrayList<ImageTemplate>();

    public Preset(String presetName, List<ImageTemplate> templates){
        setName(presetName);
        setTemplates(templates);
    }

    public void setName(String presetName) {
        this.name = presetName.length() > 0 ? presetName : "unnamed preset";
    }

    public String getName() {
        return this.name;
    }

    public void setTemplates(List<ImageTemplate> templates) {
        this.templates = templates;
    }

    public List<ImageTemplate> getTemplates() {
        return this.templates;
    }

    public boolean isTemplateNameAlreadyPresent(String name, Integer excludedIndex) {
        for (int i = 0; i < templates.size(); i++) {
            if (templates.get(i).getTemplateName().equals(name) && (excludedIndex==null || i != excludedIndex)){
                return true;
            }
        }
        for (ImageTemplate template : templates) {

        }
        return false;
    }

    public boolean addTemplate(ImageTemplate imageTemplate){
        try {
            if(isTemplateNameAlreadyPresent(imageTemplate.getTemplateName(), null)){
                return false;
            }
            this.templates.add(imageTemplate);
            return this.templates.getLast().getTemplateName().equals(imageTemplate.getTemplateName());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean editTemplate(ImageTemplate newTemplateData, int indexOfPresetToReplace){
        try {
            if(isTemplateNameAlreadyPresent(newTemplateData.getTemplateName(), indexOfPresetToReplace)){
                return false;
            }
            ImageTemplate templateToEdit = this.templates.get(indexOfPresetToReplace);

            templateToEdit.setTemplateName(newTemplateData.getTemplateName());
            templateToEdit.setNewImageBaseName(newTemplateData.getNewImageBaseName());
            templateToEdit.setNewImagePrefix(newTemplateData.getNewImagePrefix());
            templateToEdit.setNewImageSuffix(newTemplateData.getNewImageSuffix());
            templateToEdit.setWidth(newTemplateData.getWidth());
            templateToEdit.setHeight(newTemplateData.getHeight());
            templateToEdit.setResolution(newTemplateData.getResolution());
            templateToEdit.setFormat(newTemplateData.getFormat());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
