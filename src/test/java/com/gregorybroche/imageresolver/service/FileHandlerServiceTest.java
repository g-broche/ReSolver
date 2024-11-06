package com.gregorybroche.imageresolver.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@TestPropertySource(locations = "classpath:custom.properties")
public class FileHandlerServiceTest {
    private FileHandlerService fileHandlerService;

    @Autowired
    private ApplicationContext applicationContext;
    private ImageEditorService imageEditorService;
    private BufferedImage testImageBufferedContent;

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

    Path testTempDirectory;
    Path testAppDirectory;

    File testImageFile;

    @BeforeEach
    void setUp() throws IOException {
        ValidatorService validatorService = new ValidatorService();
        UserDialogService userDialogService = new UserDialogService(validatorService);
        XmlService xmlService = new XmlService();
        imageEditorService = new ImageEditorService(validatorService, userDialogService);
        fileHandlerService = new FileHandlerService(
                applicationContext,
                userDialogService,
                xmlService,
                mainDirectoryName,
                tempDirectoryName,
                presetFileName,
                windowsAppMainDirectoryRoot,
                unixAppMainDirectoryRoot
                );

        String testAppPath = fileHandlerService.getAppDirectoryPath().toString() + "-test";
        testAppDirectory = Paths.get(testAppPath);
        ReflectionTestUtils.setField(fileHandlerService, "appDirectoryPath", testAppDirectory);

        String testTempPath = fileHandlerService.getAppTempDirectoryPath().toString() + "-test";
        testTempDirectory = Paths.get(testTempPath);
        ReflectionTestUtils.setField(fileHandlerService, "appTempDirectoryPath", testTempDirectory);

        testImageBufferedContent = imageEditorService.createTestImageContent();
    }

    void tearDownTestFolder(Path TestFolderToDestroy) {
        try {
            Files.walk(TestFolderToDestroy)
                    .sorted((path1, path2) -> path2.compareTo(path1)) // reverse order to delete directories last
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * ***** testing of createAppFolder *****
     */

    @Test
    void createAppFolder_appFolderDoesNotExist_shouldCreateAppFolder() {
        try {
            assertFalse(Files.exists(testAppDirectory));

            fileHandlerService.createAppFolder();

            assertTrue(Files.exists(testAppDirectory), "Directory should exist");

        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        } finally {
            tearDownTestFolder(testAppDirectory);
        }
    }

    @Test
    void createAppFolder_appFolderExists_ShouldNotRaiseException() {
        try {
            Files.createDirectory(this.testAppDirectory);

            assertTrue(Files.exists(testAppDirectory), "Directory should exist before testing method");

            fileHandlerService.createAppFolder();

            assertTrue(Files.exists(testAppDirectory), "Directory should still exist");

        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        } finally {
            tearDownTestFolder(testAppDirectory);
        }
    }

    /*
     * ***** testing of createTempFolder *****
     */

    @Test
    void createTempFolder_tempFolderDoesNotExist_shouldCreateTempFolder() {
        try {
            assertFalse(Files.exists(testTempDirectory));

            fileHandlerService.createTempFolder();

            assertTrue(Files.exists(testTempDirectory), "Directory should exist");

        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        } finally {
            tearDownTestFolder(testTempDirectory);
        }
    }

    @Test
    void createTempFolder_tempFolderExists_ShouldNotRaiseException() {
        try {
            Files.createDirectory(this.testTempDirectory);

            assertTrue(Files.exists(testTempDirectory), "Directory should exist before testing method");

            fileHandlerService.createTempFolder();

            assertTrue(Files.exists(testTempDirectory), "Directory should still exist");

        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        } finally {
            tearDownTestFolder(testTempDirectory);
        }
    }

    /*
     * ***** testing of saveFileToTemp *****
     */

    @Test
    void saveFileToTemp_tempFolderDoesNotExist_shouldCreateTempDirectoryAndSaveFile() {
        try {
            System.out.println(imageEditorService);
            // creating valid image for testing
            File testImageFile = File.createTempFile("testValidImage", ".png");
            imageEditorService.createPNGImage(testImageBufferedContent, testImageFile);

            assertFalse(Files.exists(testTempDirectory));

            Path savedFilePath = fileHandlerService.saveFileToTemp(testImageFile);
            assertAll(
                    () -> assertTrue(Files.exists(testTempDirectory), "Directory should exist"),
                    () -> assertTrue(Files.exists(savedFilePath), "File should exist"));
        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        } finally {
            tearDownTestFolder(testTempDirectory);
        }
    }
}
