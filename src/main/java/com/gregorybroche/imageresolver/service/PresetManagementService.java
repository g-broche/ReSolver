package com.gregorybroche.imageresolver.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.classes.Preset;

@Service
public class PresetManagementService {
    private Map<String, Preset> presets = new HashMap<String, Preset>();

    public void loadPresets(){
        List<Preset> presetList = getPresetsFromFile();
        setPresets(presetList);
    }
    public void setPresets(List<Preset> presetList){
        presetList.forEach(preset -> {
            this.presets.put(preset.getName(), preset);
        });
    }

    //placeholder to maintain logic, to replace with proper method once saving to file is implemented
    public List<Preset> getPresetsFromFile(){
        List<Preset> presetList = new ArrayList<Preset>();
        Preset testPreset = new Preset("test", new ArrayList<ImageTemplate>());
        addTestTemplates(testPreset);
        presetList.add(testPreset);
        return presetList;
    }

    /**
     * Appends predifined templates to a preset for easier testing of GUI interactions without needing to add templates manually every time
     * @param preset
     */
    private void addTestTemplates(Preset preset){
        ImageTemplate templateTest1 = new ImageTemplate(
            "templateTest1",
            1920,
            1080,
            96,
            "XL",
            "test-image",
            null,
            "jpg"
        );
    
        ImageTemplate templateTest2 = new ImageTemplate(
            "templateTest2",
            1080,
            720,
            96,
            "L",
            "test-image",
            null,
            "jpg"
        );
    
        ImageTemplate templateTest3 = new ImageTemplate(
            "templateTest3",
            720,
            480,
            96,
            "M",
            "test-image",
            null,
            "jpg"
        );
    
        ImageTemplate templateTest4 = new ImageTemplate(
            "templateTest4",
            360,
            240,
            96,
            "S",
            "test-image",
            null,
            "jpg"
        );

        preset.addTemplate(templateTest1);
        preset.addTemplate(templateTest2);
        preset.addTemplate(templateTest3);
        preset.addTemplate(templateTest4);
        
    }

    public Map<String, Preset> getPresets() {
        return presets;
    }

    public Preset getPresetFromKey(String key) {
        return presets.get(key);
    }

    /**
     * add a template to one of the loaded presets using its name key for reference
     * @param template template to add
     * @param presetKey name of the preset to which template must be added
     * @return true if operation is successfull, otherwise false
     */
    public boolean addTemplateToPreset(ImageTemplate template, String presetKey) {
        try {
            Preset presetToModify = presets.get(presetKey);
            return presetToModify.addTemplate(template);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * modifies the data of a template inside of a preset
     * @param newTemplateData valid ImageTemplate instance providing the data
     * @param indexOfPresetToReplace index of the template to modify in the preset template list
     * @param presetKey key identifying the preset on which a template needs editing
     * @return true if operation has been successful, otherwise false
     */
    public boolean editTemplateOfPreset(ImageTemplate newTemplateData, int indexOfPresetToReplace, String presetKey){
        try {
            Preset presetToModify = presets.get(presetKey);
            return presetToModify.editTemplate(newTemplateData, indexOfPresetToReplace);
        } catch (Exception e) {
            return false;
        }
    }
}
