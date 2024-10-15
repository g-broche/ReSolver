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

    public boolean isTemplateNameAlreadyPresent(String name) {
        for (ImageTemplate template : templates) {
            if (template.getTemplateName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public boolean addTemplate(ImageTemplate imageTemplate){
        try {
            if(isTemplateNameAlreadyPresent(imageTemplate.getTemplateName())){
                return false;
            }
            this.templates.add(imageTemplate);
            return this.templates.getLast().getTemplateName().equals(imageTemplate.getTemplateName());
        } catch (Exception e) {
            return false;
        }
    }

}
