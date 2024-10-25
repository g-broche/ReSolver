package com.gregorybroche.imageresolver.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.classes.Preset;

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
            System.err.println(e.getMessage());
            e.printStackTrace();
            fail();
        }
    }
}
