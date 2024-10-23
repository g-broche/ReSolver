package com.gregorybroche.imageresolver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    private Preset preset1 = new Preset("preset1", new ArrayList<ImageTemplate>());
    private Preset preset2 = new Preset("preset2", new ArrayList<ImageTemplate>());
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

    @Test
    void addTemplateToPreset_addingValidTemplate_shouldReturnTrue(){
        try {
            List<Preset> presetList = new ArrayList<Preset>();
            List<ImageTemplate> presetTestContent = new ArrayList<ImageTemplate>();
            Preset preset = new Preset("presetTest", presetTestContent);
            presetList.add(preset);
            presetManagementService.setPresets(presetList);
        
            boolean result = presetManagementService.addTemplateToPreset(templateTest1, "presetTest");
            assertTrue(result);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void addTemplateToPreset_addingNull_shouldReturnFalse(){
        try {
            List<Preset> presetList = new ArrayList<Preset>();
            List<ImageTemplate> presetTestContent = new ArrayList<ImageTemplate>();
            Preset preset = new Preset("presetTest", presetTestContent);
            presetList.add(preset);
            presetManagementService.setPresets(presetList);
        
            boolean result = presetManagementService.addTemplateToPreset(null, "presetTest");
            assertFalse(result);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void addTemplateToPreset_addingTemplateWithNameCollision_shouldReturnFalse(){
        try {
            List<Preset> presetList = new ArrayList<Preset>();
            List<ImageTemplate> presetTestContent = new ArrayList<ImageTemplate>();
            Preset preset = new Preset("presetTest", presetTestContent);
            presetList.add(preset);
            presetManagementService.setPresets(presetList);
        
            boolean resultFirstAddition = presetManagementService.addTemplateToPreset(templateTest1, "presetTest");
            assertTrue(resultFirstAddition);

            boolean resultCollision = presetManagementService.addTemplateToPreset(templateTest1, "presetTest");
            assertFalse(resultCollision);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void editTemplateOfPreset_presetKeyIsNull_shouldReturnFalseAndUnchangedTemplateWhenEditing(){
        try {
            preset1.addTemplate(templateTest1);
            List<Preset> testPresets = new ArrayList<Preset>();
            testPresets.add(preset1);
            testPresets.add(preset2);
            presetManagementService.setPresets(testPresets);
            assertEquals(presetManagementService.getPresetFromKey("preset1").getName(), "preset1");
            assertEquals(presetManagementService.getPresetFromKey("preset2").getName(), "preset2");
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().getFirst().getTemplateName(), "templateTest1");
            boolean editResult = presetManagementService.editTemplateOfPreset(templateTest3, 0, null);
            assertFalse(editResult);
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().getFirst().getTemplateName(), "templateTest1");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void editTemplateOfPreset_presetKeyIsWrong_shouldReturnFalseAndUnchangedTemplateWhenEditing(){
        try {
            preset1.addTemplate(templateTest1);
            List<Preset> testPresets = new ArrayList<Preset>();
            testPresets.add(preset1);
            testPresets.add(preset2);
            presetManagementService.setPresets(testPresets);
            assertEquals(presetManagementService.getPresetFromKey("preset1").getName(), "preset1");
            assertEquals(presetManagementService.getPresetFromKey("preset2").getName(), "preset2");
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().getFirst().getTemplateName(), "templateTest1");
            boolean editResult = presetManagementService.editTemplateOfPreset(templateTest3, 0, "wrongKey");
            assertFalse(editResult);
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().getFirst().getTemplateName(), "templateTest1");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void editTemplateOfPreset_presetKeyIsRightButTemplateNameTaken_shouldReturnFalseAndUnchangedTemplateWhenEditing(){
        try {
            preset1.addTemplate(templateTest1);
            preset1.addTemplate(templateTest2);
            preset1.addTemplate(templateTest3);
            List<Preset> testPresets = new ArrayList<Preset>();
            testPresets.add(preset1);
            testPresets.add(preset2);
            presetManagementService.setPresets(testPresets);
            assertEquals(presetManagementService.getPresetFromKey("preset1").getName(), "preset1");
            assertEquals(presetManagementService.getPresetFromKey("preset2").getName(), "preset2");
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().getFirst().getTemplateName(), "templateTest1");
            ImageTemplate editSourceTemplateWithNameCollision = new ImageTemplate(
                "templateTest2",
                1081,
                721,
                90,
                "L_edit-",
                "test_edit",
                "-edited_ver",
                "webp"
                );
            boolean editResult = presetManagementService.editTemplateOfPreset(
                editSourceTemplateWithNameCollision,
                0,
                "preset1"
                );
            assertFalse(editResult);
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().getFirst().getTemplateName(), "templateTest1");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void editTemplateOfPreset_presetKeyIsRightAndEditTemplateDataIsValid_shouldReturnTrueAndChangedTemplateWhenEditing(){
        try {
            preset1.addTemplate(templateTest1);
            preset1.addTemplate(templateTest2);
            preset1.addTemplate(templateTest3);
            List<Preset> testPresets = new ArrayList<Preset>();
            testPresets.add(preset1);
            testPresets.add(preset2);
            presetManagementService.setPresets(testPresets);
            assertEquals(presetManagementService.getPresetFromKey("preset1").getName(), "preset1");
            assertEquals(presetManagementService.getPresetFromKey("preset2").getName(), "preset2");
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().getFirst().getTemplateName(), "templateTest1");
            ImageTemplate editSourceTemplate = new ImageTemplate(
                "templateTest1-edited",
                1921,
                1081,
                90,
                "XL_edit-",
                "test_edit",
                "-edited_ver",
                "webp"
                );
            boolean editResult = presetManagementService.editTemplateOfPreset(
                editSourceTemplate,
                0,
                "preset1"
                );
            assertTrue(editResult);
            ImageTemplate editedTemplate = presetManagementService.getPresetFromKey("preset1").getTemplates().getFirst();
            assertEquals(editedTemplate.getTemplateName(), "templateTest1-edited");
            assertEquals(editedTemplate.getWidth(), 1921);
            assertEquals(editedTemplate.getHeight(), 1081);
            assertEquals(editedTemplate.getResolution(), 90);
            assertEquals(editedTemplate.getNewImagePrefix(), "XL_edit-");
            assertEquals(editedTemplate.getNewImageBaseName(), "test_edit");
            assertEquals(editedTemplate.getNewImageSuffix(), "-edited_ver");
            assertEquals(editedTemplate.getFormat(), "webp");
        } catch (Exception e) {
            fail();
        }
    }

    
    @Test
    void deleteTemplateOfPreset_presetKeyIsNull_shouldReturnFalseAndUnchangedTemplatesWhenDeleting(){
        try {
            preset1.addTemplate(templateTest1);
            preset1.addTemplate(templateTest2);
            preset1.addTemplate(templateTest3);
            List<Preset> testPresets = new ArrayList<Preset>();
            testPresets.add(preset1);
            testPresets.add(preset2);
            presetManagementService.setPresets(testPresets);
            boolean deleteResult = presetManagementService.deleteTemplateOfPreset(0, null);
            assertFalse(deleteResult);
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().getFirst().getTemplateName(), "templateTest1");
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().size(), 3);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void deleteTemplateOfPreset_presetKeyIsWrong_shouldReturnFalseAndUnchangedTemplatesWhenDeleting(){
        try {
            preset1.addTemplate(templateTest1);
            preset1.addTemplate(templateTest2);
            preset1.addTemplate(templateTest3);
            List<Preset> testPresets = new ArrayList<Preset>();
            testPresets.add(preset1);
            testPresets.add(preset2);
            presetManagementService.setPresets(testPresets);
            boolean deleteResult = presetManagementService.deleteTemplateOfPreset(0, "wrongKey");
            assertFalse(deleteResult);
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().getFirst().getTemplateName(), "templateTest1");
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().size(), 3);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void deleteTemplateOfPreset_presetKeyIsRightButIndexIsOutOfBounds_shouldReturnFalseAndUnchangedTemplatesWhenDeleting(){
        try {
            preset1.addTemplate(templateTest1);
            preset1.addTemplate(templateTest2);
            preset1.addTemplate(templateTest3);
            List<Preset> testPresets = new ArrayList<Preset>();
            testPresets.add(preset1);
            testPresets.add(preset2);
            presetManagementService.setPresets(testPresets);
            boolean editResult = presetManagementService.deleteTemplateOfPreset(3, "preset1");
            assertFalse(editResult);
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().getFirst().getTemplateName(), "templateTest1");
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().size(), 3);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void deleteTemplateOfPreset_presetKeyIsRightAndIndexIsValid_shouldReturnTrueAndRemoveTemplateWhenDeleting(){
        try {
            preset1.addTemplate(templateTest1);
            preset1.addTemplate(templateTest2);
            preset1.addTemplate(templateTest3);
            List<Preset> testPresets = new ArrayList<Preset>();
            testPresets.add(preset1);
            testPresets.add(preset2);
            presetManagementService.setPresets(testPresets);
            boolean editResult = presetManagementService.deleteTemplateOfPreset(0, "preset1");
            assertTrue(editResult);
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().getFirst().getTemplateName(), "templateTest2");
            assertEquals(presetManagementService.getPresetFromKey("preset1").getTemplates().size(), 2);
        } catch (Exception e) {
            fail();
        }
    }
}
