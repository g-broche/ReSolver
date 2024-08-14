package com.gregorybroche.imageresolver.service;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

public class ValidatorServiceTest {
    private final List<String> testAllowedImageFormats = Arrays.asList("*.jpg", "*.jpeg", "*.png", "*.bmp", "*.webp");
    private final List<String> testAllowedImageMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/bmp", "image/webp");
    ValidatorService validatorService;
    ImageEditorService imageEditorService;
    BufferedImage testImageBufferedContent;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp(){
        validatorService = new ValidatorService(); 
        UserDialogService userDialogService = new UserDialogService(validatorService);
        imageEditorService = new ImageEditorService(validatorService, userDialogService);
        ReflectionTestUtils.setField(validatorService, "allowedImageMimeTypes", testAllowedImageMimeTypes);
        ReflectionTestUtils.setField(validatorService, "allowedImageFormats", testAllowedImageFormats);
        testImageBufferedContent = imageEditorService.createTestImageContent();
    }

    /*
     * ***** testing of getAllowedImageFormats *****
     */


    @Test
    void getAllowedImageFormats_shouldReturnListAsDefined() {
        try {
            List<String> allowedFormatResult = this.validatorService.getAllowedImageFormats();
            assertTrue(allowedFormatResult.equals(testAllowedImageFormats), "Set property and getter return should match");
        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        }
    }

    /*
     * ***** testing of getAllowedImageFormatsAsString *****
     */

    @Test
    void getAllowedImageFormatsAsString_shouldReturnStringInExpectedFormat() {
        try {
            String expectedStringifiedValue = ".jpg, .jpeg, .png, .bmp, .webp";
            String allowedFormatResultAsString = this.validatorService.getAllowedImageFormatsAsString();
            assertTrue(allowedFormatResultAsString.equals(expectedStringifiedValue), "Should return string: "+expectedStringifiedValue);
        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        }
    }

    /*
     * ***** testing of isFileValidImageFormat *****
     */

    @Test
    void isFileValidImageFormat_fileAsPNG_ShouldReturnTrue() {
        try {
            File testPNGFile = tempDir.resolve("testImage.png").toFile();
            imageEditorService.createPNGImage(testImageBufferedContent, testPNGFile);
            assertTrue(validatorService.isFileValidImageFormat(testPNGFile));
        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        }
    }

    @Test
    void isFileValidImageFormat_fileAsJPG_ShouldReturnTrue() {
        try {
            File testJPGFile = tempDir.resolve("testImage.jpg").toFile();
            imageEditorService.createPNGImage(testImageBufferedContent, testJPGFile);
            assertTrue(validatorService.isFileValidImageFormat(testJPGFile));
        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        }
    }

    @Test
    void isFileValidImageFormat_fileAsJPEG_ShouldReturnTrue() {
        try {
            File testJPEGFile = tempDir.resolve("testImage.jpeg").toFile();
            imageEditorService.createPNGImage(testImageBufferedContent, testJPEGFile);
            assertTrue(validatorService.isFileValidImageFormat(testJPEGFile));
        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        }
    }

    @Test
    void isFileValidImageFormat_fileAsBMP_ShouldReturnTrue() {
        try {
            File testBMPFile = tempDir.resolve("testImage.bmp").toFile();
            imageEditorService.createPNGImage(testImageBufferedContent, testBMPFile);
            assertTrue(validatorService.isFileValidImageFormat(testBMPFile));
        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        }
    }
    @Test
    void isFileValidImageFormat_fileAsWEBP_ShouldReturnTrue() {
        try {
            File testWEBPFile = tempDir.resolve("testImage.webp").toFile();
            imageEditorService.createPNGImage(testImageBufferedContent, testWEBPFile);
            assertTrue(validatorService.isFileValidImageFormat(testWEBPFile));
        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        }
    }
    @Test
    void isFileValidImageFormat_fileAsTXT_ShouldReturnFalse() {
        try {
            File testTXTFile = tempDir.resolve("testNonImage.txt").toFile();
            List<String> lines = Arrays.asList("1", "2", "3");
            Files.write(testTXTFile.toPath(), lines);
            assertFalse(validatorService.isFileValidImageFormat(testTXTFile));
        } catch (Exception e) {
            fail("Exception triggered " + e.getMessage());
        }
    }
}
