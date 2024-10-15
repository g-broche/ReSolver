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
        presetList.add(testPreset);
        return presetList;
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
            if (presetToModify.isTemplateNameAlreadyPresent(template.getTemplateName())) {
                return false;
            }
            return presetToModify.addTemplate(template);
        } catch (Exception e) {
            return false;
        }
        
    }
}
