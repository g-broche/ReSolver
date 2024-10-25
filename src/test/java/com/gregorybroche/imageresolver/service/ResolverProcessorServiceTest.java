package com.gregorybroche.imageresolver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.classes.Preset;
import com.gregorybroche.imageresolver.classes.ValidationResponse;

@SpringBootTest
public class ResolverProcessorServiceTest {
    ResolverProcessorService resolverProcessorService;
    ImageEditorService imageEditorService;
    private Path tempDir;
    private BufferedImage testSourceImageContent;
    private Preset preset = new Preset("preset1", new ArrayList<ImageTemplate>());
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

    private ImageTemplate invalidFormatTemplate = new ImageTemplate(
            "testTemplate",
            1920,
            1080,
            96,
            "XL",
            "test-image",
            null,
            "txt"
            );

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${resolver.directory.main}")
    private String mainDirectoryName;

    @Value("${resolver.directory.temp}")
    private String tempDirectoryName;

    @Value("${resolver.file.presets}")
    private String presetFileName;

    @Value("${resolver.directory.windows}")
    private String windowsAppMainDirectoryRoot;

    @Value("${resolver.directory.unix}")
    private String unixAppMainDirectoryRoot;

    @BeforeEach
    void setUp() throws IOException {
        ValidatorService validatorService = new ValidatorService();
        UserDialogService userDialogService = new UserDialogService(validatorService);
        FileHandlerService fileHandlerService = new FileHandlerService(
            applicationContext,
            userDialogService,
            mainDirectoryName,
            tempDirectoryName,
            presetFileName,
            windowsAppMainDirectoryRoot,
            unixAppMainDirectoryRoot
            );
        imageEditorService = new ImageEditorService(validatorService, userDialogService);
        resolverProcessorService = new ResolverProcessorService(imageEditorService, fileHandlerService, validatorService);
        tempDir = Files.createTempDirectory("testResolverProcessorService");
        testSourceImageContent = imageEditorService.createTestImageContent();

    }

    @AfterEach
    void tearDownTestFolder() {
        try {
            if (tempDir != null && Files.exists(tempDir)) {
                Files.walk(tempDir)
                        .sorted((path1, path2) -> path2.compareTo(path1))
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void processImageForTemplate_argumentsAreValid_shouldReturnCreatedFile() {
        try {
            File createdFile = resolverProcessorService.processImageForTemplate(testSourceImageContent, templateTest1, tempDir);
            assertTrue(Files.exists(createdFile.toPath()));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void processImageForTemplate_imageContentIsMissing_shouldThrowIOException() {
        assertThrows(IOException.class, ()->{
            resolverProcessorService.processImageForTemplate(null, templateTest1, tempDir);
        });
    }

    @Test
    void processImageForTemplate_templateIsMissing_shouldThrowIOException() {
        assertThrows(IOException.class, ()->{
            resolverProcessorService.processImageForTemplate(testSourceImageContent, null, tempDir);
        });
    }

    @Test
    void processImageForTemplate_directoryIsMissing_shouldThrowIOException() {
        assertThrows(IOException.class, ()->{
            resolverProcessorService.processImageForTemplate(testSourceImageContent, templateTest1, null);
        });
    }

    @Test
    void processImageForTemplate_templateFormatIsInvalid_shouldThrowIOException() {
        assertThrows(IOException.class, ()->{
            resolverProcessorService.processImageForTemplate(testSourceImageContent, invalidFormatTemplate, tempDir);
        });
    }

    @Test
    void resolveImageForAllTemplates_ArgumentsAreValid_ShouldReturnSuccessValidationResponseAndHaveCreatedAllImages(){
        List<ImageTemplate> validTemplates = new ArrayList<ImageTemplate>();
        validTemplates.add(templateTest1);
        validTemplates.add(templateTest2);
        validTemplates.add(templateTest3);
        validTemplates.add(templateTest4);
        ValidationResponse resolveResult = resolverProcessorService.resolveImageForAllTemplates(testSourceImageContent, validTemplates, tempDir);
        assertTrue(resolveResult.isSuccess());
        Path pathFirstImage = Paths.get(tempDir.toString()+'\\'+templateTest1.getCompleteFileName());
        Path pathSecondImage = Paths.get(tempDir.toString()+'\\'+templateTest2.getCompleteFileName());
        Path pathThirdImage = Paths.get(tempDir.toString()+'\\'+templateTest3.getCompleteFileName());
        Path pathFourthImage = Paths.get(tempDir.toString()+'\\'+templateTest4.getCompleteFileName());
        System.out.println(pathFirstImage);
        assertTrue(Files.exists(pathFirstImage));
        assertTrue(Files.exists(pathSecondImage));
        assertTrue(Files.exists(pathThirdImage));
        assertTrue(Files.exists(pathFourthImage));
    }

    @Test
    void resolveImageForAllTemplates_nullImageContent_ShouldReturnErrorValidationResponseAndCorrespondingMessage(){
        List<ImageTemplate> validTemplates = new ArrayList<ImageTemplate>();
        validTemplates.add(templateTest1);
        validTemplates.add(templateTest2);
        validTemplates.add(templateTest3);
        validTemplates.add(templateTest4);
        ValidationResponse resolveResult = resolverProcessorService.resolveImageForAllTemplates(null, validTemplates, tempDir);
        assertFalse(resolveResult.isSuccess());
        assertEquals(resolveResult.getMessage(), "source image content is null");
    }

    @Test
    void resolveImageForAllTemplates_nullTemplate_ShouldReturnErrorValidationResponseAndCorrespondingMessage(){
        ValidationResponse resolveResult = resolverProcessorService.resolveImageForAllTemplates(testSourceImageContent, null, tempDir);
        assertFalse(resolveResult.isSuccess());
        assertEquals(resolveResult.getMessage(), "templates list is null");
    }

    @Test
    void resolveImageForAllTemplates_emptyTemplateList_ShouldReturnErrorValidationResponseAndCorrespondingMessage(){
        List<ImageTemplate> validTemplates = new ArrayList<ImageTemplate>();
        ValidationResponse resolveResult = resolverProcessorService.resolveImageForAllTemplates(testSourceImageContent, validTemplates, tempDir);
        assertFalse(resolveResult.isSuccess());
        assertEquals(resolveResult.getMessage(), "templates list is empty");
    }
    
    @Test
    void resolveImageForAllTemplates_nullDirectory_ShouldReturnErrorValidationResponseAndCorrespondingMessage(){
        List<ImageTemplate> validTemplates = new ArrayList<ImageTemplate>();
        validTemplates.add(templateTest1);
        validTemplates.add(templateTest2);
        validTemplates.add(templateTest3);
        validTemplates.add(templateTest4);
        ValidationResponse resolveResult = resolverProcessorService.resolveImageForAllTemplates(testSourceImageContent, validTemplates, null);
        assertFalse(resolveResult.isSuccess());
        assertEquals(resolveResult.getMessage(), "save directory is null");
    }
    
    @Test
    void resolveImageForAllTemplates_allArgumentsAreNull_ShouldReturnErrorValidationResponseAndCorrespondingCompoundMessage(){
        ValidationResponse resolveResult = resolverProcessorService.resolveImageForAllTemplates(null, null, null);
        assertFalse(resolveResult.isSuccess());
        assertEquals(resolveResult.getMessage(), "source image content is null, templates list is null, save directory is null");
    }

    @Test
    void resolveImageForAllTemplates_thirdTemplateIsNull_ShouldNotLeaveAnyCreatedFileAndReturnErrorValidationResponseWithExpectedMessage(){
        List<ImageTemplate> templates = new ArrayList<ImageTemplate>();
        templates.add(templateTest1);
        templates.add(templateTest2);
        templates.add(null);
        templates.add(templateTest3);
        templates.add(templateTest4);
        String expectedMessage = "an error occured while processing and operation was aborted : "+"template is null";

        ValidationResponse resolveResult = resolverProcessorService.resolveImageForAllTemplates(testSourceImageContent, templates, tempDir);
        assertFalse(resolveResult.isSuccess());
        assertEquals(expectedMessage, resolveResult.getMessage());
        Path pathFirstImage = Paths.get(tempDir.toString()+'\\'+templateTest1.getCompleteFileName());
        Path pathSecondImage = Paths.get(tempDir.toString()+'\\'+templateTest2.getCompleteFileName());
        Path pathFourthImage = Paths.get(tempDir.toString()+'\\'+templateTest3.getCompleteFileName());
        Path pathFifthImage = Paths.get(tempDir.toString()+'\\'+templateTest4.getCompleteFileName());
        assertFalse(Files.exists(pathFirstImage));
        assertFalse(Files.exists(pathSecondImage));
        assertFalse(Files.exists(pathFourthImage));
        assertFalse(Files.exists(pathFifthImage));
    }
}
