package com.gregorybroche.imageresolver.classes;

import java.util.ArrayList;
import java.util.List;

public class Preset {
    private String name;
    private List<ImageTemplate> templates = new ArrayList<ImageTemplate>();

    public Preset(String presetName){
        setName(presetName);
    }

    public void setName(String presetName) {
        this.name = presetName.length() > 0 ? presetName : "unnamed preset";
    }
    public String getTemplateName() {
        return this.name;
    }

    public List<ImageTemplate> getTemplates() {
        return this.templates;
    }

    public boolean addTemplate(ImageTemplate imageTemplate){
        try {
            this.templates.add(imageTemplate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
