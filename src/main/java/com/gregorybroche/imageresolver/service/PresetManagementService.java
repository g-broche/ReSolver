package com.gregorybroche.imageresolver.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.classes.Preset;

@Service
public class PresetManagementService {
    private Map<String, Preset> presets = new HashMap<String, Preset>();

    public void loadPresets(){
        Preset testPreset = new Preset("test", new ArrayList<ImageTemplate>());
        this.presets.put(testPreset.getName(), testPreset);
    }
    public Map<String, Preset> getPresets() {
        return presets;
    }
    public Preset getPresetByKey(String key) {
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
