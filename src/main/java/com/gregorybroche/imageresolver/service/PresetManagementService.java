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
    public boolean addTemplateToPreset(ImageTemplate template, String presetKey) {
        try {
            presets.get(presetKey).addTemplate(template);
            return true;
        } catch (Exception e) {
            return false;
        }
        
    }
}
