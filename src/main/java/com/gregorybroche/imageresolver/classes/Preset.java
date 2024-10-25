package com.gregorybroche.imageresolver.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Preset {
    private String name;
    private List<ImageTemplate> templates = new ArrayList<ImageTemplate>();

    public Preset(String presetName, List<ImageTemplate> templates) {
        setName(presetName);
        templates = templates != null ? templates : new ArrayList<ImageTemplate>();
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

    /**
     * comparator to sort templates by decreasing width property
     */
    public static Comparator<ImageTemplate> decreasingWidthComparator = new Comparator<ImageTemplate>() {
        public int compare(ImageTemplate template1, ImageTemplate template2){
            int width1 = template1.getWidth();
            int width2 = template2.getWidth();
            return width2 - width1;
        }
    };

    /**
     * checks if a name string already corresponds to an existing template name in
     * the template list
     * 
     * @param templateName  string of the name to test
     * @param excludedIndex use an index to ignore a specific template from the
     *                      check, used in case of validating edit action with an
     *                      unchanged
     *                      template name
     * @return true if a template matches the given string, otherwise false
     */
    public boolean isTemplateNameAlreadyPresent(String templateName, Integer excludedIndex) {
        for (int i = 0; i < templates.size(); i++) {
            if (templates.get(i).getTemplateName().equals(templateName)
                    && (excludedIndex == null || i != excludedIndex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a template to the list of templates
     * 
     * @param imageTemplate
     * @return true if adding was successful, otherwise false
     */
    public boolean addTemplate(ImageTemplate imageTemplate) {
        try {
            if (isTemplateNameAlreadyPresent(imageTemplate.getTemplateName(), null)) {
                return false;
            }
            this.templates.add(imageTemplate);
            return this.templates.getLast().getTemplateName().equals(imageTemplate.getTemplateName());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Edits a template using an other template's data
     * 
     * @param newTemplateData
     * @param indexOfTemplateToReplace index of template to edit in this preset's
     *                                 template list
     * @return true if editing the template's properties was successful, otherwise
     *         false
     */
    public boolean editTemplate(ImageTemplate newTemplateData, int indexOfTemplateToReplace) {
        try {
            if (isTemplateNameAlreadyPresent(newTemplateData.getTemplateName(), indexOfTemplateToReplace)) {
                return false;
            }
            ImageTemplate templateToEdit = this.templates.get(indexOfTemplateToReplace);

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

    /**
     * delete template from this preset
     * @param indexOfTemplateToDelete
     * @return true if delete is successful, otherwise false
     */
    public boolean deleteTemplate(int indexOfTemplateToDelete) {
        try {
            if (this.templates.size() < indexOfTemplateToDelete) {
                return false;
            }
            this.templates.remove(indexOfTemplateToDelete);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void orderTemplatesByWidth(){
        Collections.sort(this.templates, Preset.decreasingWidthComparator);
    }

}
