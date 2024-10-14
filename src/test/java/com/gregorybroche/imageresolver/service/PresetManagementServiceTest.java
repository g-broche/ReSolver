package com.gregorybroche.imageresolver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.classes.Preset;

public class PresetManagementServiceTest {
    private PresetManagementService presetManagementService;
    private ImageTemplate templateTest1 = new ImageTemplate(
        "templateTest1",
        1920,
        1080,
        96,
        "XL",
        "test-image",
        null,
        "jpg"
        );

    private ImageTemplate templateTest2 = new ImageTemplate(
        "templateTest2",
        1080,
        720,
        96,
        "L",
        "test-image",
        null,
        "jpg"
        );

    private ImageTemplate templateTest3 = new ImageTemplate(
        "templateTest3",
        720,
        480,
        96,
        "M",
        "test-image",
        null,
        "jpg"
        );

    private ImageTemplate templateTest4 = new ImageTemplate(
        "templateTest4",
        360,
        240,
        96,
        "S",
        "test-image",
        null,
        "jpg"
        );

    public PresetManagementServiceTest(){
        this.presetManagementService = new PresetManagementService();
    }

    @Test
    void setPresets_inputListIsEmpty_shouldReturnEmptyMap(){
        try {
            List<Preset> emptyPresetList = new ArrayList<Preset>();
            presetManagementService.setPresets(emptyPresetList);
            Map<String, Preset> retrievedPresetList = presetManagementService.getPresets();
            assertTrue(retrievedPresetList.isEmpty());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void setPresets_inputListWithTwoPresets_shouldAddBothPresets(){
        try {
            List<ImageTemplate> preset1Content = new ArrayList<ImageTemplate>();
            preset1Content.add(templateTest1);
            preset1Content.add(templateTest2);
            preset1Content.add(templateTest3);
            preset1Content.add(templateTest4);

            List<ImageTemplate> preset2Content = new ArrayList<ImageTemplate>();
            preset2Content.add(templateTest3);
            preset2Content.add(templateTest4);

            Preset preset1 = new Preset("presetTest1", preset1Content);
            Preset preset2 = new Preset("presetTest2", preset2Content);

            List<Preset> presetList = new ArrayList<Preset>();
            presetList.add(preset1);
            presetList.add(preset2);

            presetManagementService.setPresets(presetList);
            String nameFirstTemplateOfFirstSetPreset = presetManagementService.getPresetFromKey("presetTest1").getTemplates().get(0).getTemplateName();
            String nameSecondTemplateOfFirstSetPreset = presetManagementService.getPresetFromKey("presetTest1").getTemplates().get(1).getTemplateName();
            String nameThirdTemplateOfFirstSetPreset = presetManagementService.getPresetFromKey("presetTest1").getTemplates().get(2).getTemplateName();
            String nameFourthTemplateOfFirstSetPreset = presetManagementService.getPresetFromKey("presetTest1").getTemplates().get(3).getTemplateName();

            String nameFirstTemplateOfSecondSetPreset = presetManagementService.getPresetFromKey("presetTest2").getTemplates().get(0).getTemplateName();
            String nameSecondTemplateOfSecondSetPreset = presetManagementService.getPresetFromKey("presetTest2").getTemplates().get(1).getTemplateName();
            
            assertEquals(nameFirstTemplateOfFirstSetPreset, templateTest1.getTemplateName());
            assertEquals(nameSecondTemplateOfFirstSetPreset, templateTest2.getTemplateName());
            assertEquals(nameThirdTemplateOfFirstSetPreset, templateTest3.getTemplateName());
            assertEquals(nameFourthTemplateOfFirstSetPreset, templateTest4.getTemplateName());

            assertEquals(nameFirstTemplateOfSecondSetPreset, templateTest3.getTemplateName());
            assertEquals(nameSecondTemplateOfSecondSetPreset, templateTest4.getTemplateName());

        } catch (Exception e) {
            fail();
        }
    }
}
