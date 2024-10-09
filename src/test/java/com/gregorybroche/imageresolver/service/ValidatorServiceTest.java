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

import com.gregorybroche.imageresolver.Enums.ConstraintType;
import com.gregorybroche.imageresolver.classes.InputConstraint;
import com.gregorybroche.imageresolver.classes.ValidationResponse;

public class ValidatorServiceTest {
    private final List<String> testAllowedImageFormats = Arrays.asList("*.jpg", "*.jpeg", "*.png", "*.bmp", "*.webp", "*.avif");
    private final String expectedStringifiedValue = ".jpg, .jpeg, .png, .bmp, .webp, .avif";
    private final List<String> testAllowedImageMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/bmp", "image/webp", "image/avif");
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
            testImageBufferedContent = imageEditorService.createTestImageContent();
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
            testImageBufferedContent = imageEditorService.createTestImageContent();
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
            testImageBufferedContent = imageEditorService.createTestImageContent();
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
            testImageBufferedContent = imageEditorService.createTestImageContent();
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
            testImageBufferedContent = imageEditorService.createTestImageContent();
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

    /*
    * ***** TESTING INPUT RELATED METHODS ***** 
    */

    /* ***** isValueStringCompatible ***** */

    @Test
    void isValueStringCompatible_valueAsString_ShouldReturnTrue() {
        String value = "test";
        assertTrue(validatorService.isValueStringCompatible(value));
    }

    @Test
    void isValueStringCompatible_valueAsEmptyString_ShouldReturnTrue() {
        String value = "";
        assertTrue(validatorService.isValueStringCompatible(value));
    }

    @Test
    void isValueStringCompatible_valueAsNullString_ShouldReturnFalse() {
        String value = null;
        assertFalse(validatorService.isValueStringCompatible(value));
    }

    @Test
    void isValueStringCompatible_valueAsInteger_ShouldReturnTrue() {
        Integer value = 5;
        assertTrue(validatorService.isValueStringCompatible(value));
    }

    @Test
    void isValueStringCompatible_valueAsNullInteger_ShouldReturnFalse() {
        Integer value = null;
        assertFalse(validatorService.isValueStringCompatible(value));
    }

    @Test
    void isValueStringCompatible_valueAsObject_ShouldReturnFalse() {
        Object value = new Object();
        assertFalse(validatorService.isValueStringCompatible(value));
    }

        /* ***** isNotEmpty ***** */

    @Test
    void isNotEmpty_valueAs0_ShouldReturnTrue() {
        Integer value = 0;
        assertTrue(validatorService.isNotEmpty(value));
    }

    @Test
    void isNotEmpty_valueAsNonEmptyString_ShouldReturnTrue() {
        String value = "test";
        assertTrue(validatorService.isNotEmpty(value));
    }

    @Test
    void isNotEmpty_valueAsEmptyString_ShouldReturnFalse() {
        String value = "";
        assertFalse(validatorService.isNotEmpty(value));
    }

    @Test
    void isNotEmpty_valueAsNull_ShouldReturnFalse() {
        Object value = null;
        assertFalse(validatorService.isNotEmpty(value));
    }

    /* ***** isGreaterThan ***** */

    @Test
    void isGreaterThan_valueAsNull_ShouldReturnFalse() {
        Integer value = null;
        Integer min = 5;
        assertFalse(validatorService.isGreaterThan(value, min));
    }

    @Test
    void isGreaterThan_valueBelowMin_ShouldReturnFalse() {
        Integer value = 2;
        Integer min = 5;
        assertFalse(validatorService.isGreaterThan(value, min));
    }

    @Test
    void isGreaterThan_valueAboveMin_ShouldReturnTrue() {
        Integer value = 8;
        Integer min = 5;
        assertTrue(validatorService.isGreaterThan(value, min));
    }

    @Test
    void isGreaterThan_valueAboveMinNegativeInt_ShouldReturnTrue() {
        Integer value = -3;
        Integer min = -5;
        assertTrue(validatorService.isGreaterThan(value, min));
    }

    @Test
    void isGreaterThan_valueAsGreaterString_ShouldReturnFalse() {
        String value = "8";
        Integer min = 5;
        assertFalse(validatorService.isGreaterThan(value, min));
    }

    /* ***** isLessThan ***** */

    @Test
    void isLessThan_valueAsNull_ShouldReturnFalse() {
        Integer value = null;
        Integer max = 5;
        assertFalse(validatorService.isLessThan(value, max));
    }

    @Test
    void isLessThan_valueBelowMin_ShouldReturnTrue() {
        Integer value = 2;
        Integer max = 5;
        assertTrue(validatorService.isLessThan(value, max));
    }

    @Test
    void isLessThan_valueAboveMin_ShouldReturnFalse() {
        Integer value = 8;
        Integer max = 5;
        assertFalse(validatorService.isLessThan(value, max));
    }

    @Test
    void isLessThan_valueAboveMinNegativeInt_ShouldReturnFalse() {
        Integer value = -3;
        Integer max = -5;
        assertFalse(validatorService.isLessThan(value, max));
    }
    
    @Test
    void isLessThan_valueAsLesserString_ShouldReturnFalse() {
        String value = "2";
        Integer max = 5;
        assertFalse(validatorService.isGreaterThan(value, max));
    }
    
    /* ***** isLongerThan ***** */

    @Test
    void isLongerThan_valueAsNull_ShouldReturnFalse() {
        String value = null;
        Integer minLen = 0;
        assertFalse(validatorService.isLongerThan(value, minLen));
    }

    @Test
    void isLongerThan_valueAsNeitherStringNorInteger_ShouldReturnFalse() {
        List<String> value = this.testAllowedImageFormats;
        Integer minLen = 0;
        assertFalse(validatorService.isLongerThan(value, minLen));
    }

    @Test
    void isLongerThan_valueAsShorterString_ShouldReturnFalse() {
        String value = "short";
        Integer minLen = 10;
        assertFalse(validatorService.isLongerThan(value, minLen));
    }

    @Test
    void isLongerThan_valueAsLongerString_ShouldReturnTrue() {
        String value = "longer string";
        Integer minLen = 10;
        assertTrue(validatorService.isLongerThan(value, minLen));
    }

    @Test
    void isLongerThan_valueAsShorterInteger_ShouldReturnFalse() {
        Integer value = 49;
        Integer minLen = 10;
        assertFalse(validatorService.isLongerThan(value, minLen));
    }

    @Test
    void isLongerThan_valueAsLongerInteger_ShouldReturnTrue() {
        Integer value = 58426;
        Integer minLen = 4;
        assertTrue(validatorService.isLongerThan(value, minLen));
    }
    
    /* ***** isShorterThan ***** */

    @Test
    void isShorterThan_valueAsNull_ShouldReturnFalse() {
        String value = null;
        Integer maxLen = 0;
        assertFalse(validatorService.isShorterThan(value, maxLen));
    }

    @Test
    void isShorterThan_valueAsNeitherStringNorInteger_ShouldReturnFalse() {
        List<String> value = this.testAllowedImageFormats;
        Integer maxLen = 0;
        assertFalse(validatorService.isShorterThan(value, maxLen));
    }

    @Test
    void isShorterThan_valueAsShorterString_ShouldReturnTrue() {
        String value = "short";
        Integer maxLen = 10;
        assertTrue(validatorService.isShorterThan(value, maxLen));
    }

    @Test
    void isShorterThan_valueAsLongerString_ShouldReturnFalse() {
        String value = "longer string";
        Integer maxLen = 10;
        assertFalse(validatorService.isShorterThan(value, maxLen));
    }

    @Test
    void isShorterThan_valueAsShorterInteger_ShouldReturnTrue() {
        Integer value = 49;
        Integer maxLen = 10;
        assertTrue(validatorService.isShorterThan(value, maxLen));
    }

    @Test
    void isShorterThan_valueAsLongerInteger_ShouldReturnFalse() {
        Integer value = 58426;
        Integer maxLen = 4;
        assertFalse(validatorService.isShorterThan(value, maxLen));
    }

    /* ***** isIncludedIn ***** */

    @Test
    void isIncludedIn_valueAsNullAndArrayIsEmpty_ShouldReturnFalse() {
        String value = null;
        String[] haystack = new String[5];
        assertFalse(validatorService.isIncludedIn(value, haystack));
    }

    @Test
    void isIncludedIn_valueAndArrayTypesAreMismatched_ShouldReturnFalse() {
        int value = 3;
        String[] haystack = new String[5];
        haystack[0] = "1";
        haystack[1] = "2";
        haystack[2] = "3";
        haystack[3] = "4";
        haystack[4] = "5";
        assertFalse(validatorService.isIncludedIn(value, haystack));
    }

    @Test
    void isIncludedIn_valueIsNotInArray_ShouldReturnFalse() {
        String value = "tiff";
        String[] haystack = new String[5];
        haystack[0] = "jpg";
        haystack[1] = "png";
        haystack[2] = "webp";
        haystack[3] = "jpeg";
        haystack[4] = "avif";
        assertFalse(validatorService.isIncludedIn(value, haystack));
    }

    @Test
    void isIncludedIn_valueIsInArray_ShouldReturnTrue() {
        String value = "webp";
        String[] haystack = new String[5];
        haystack[0] = "jpg";
        haystack[1] = "png";
        haystack[2] = "webp";
        haystack[3] = "jpeg";
        haystack[4] = "avif";
        assertTrue(validatorService.isIncludedIn(value, haystack));
    }

    @Test
    void isIncludedIn_valueIsNotInArrayUsingRealConstraint_ShouldReturnFalse() {
        String value = "tiff";
        TemplateFormValidatorService templateSubmitterService = new TemplateFormValidatorService(this.validatorService);
        assertFalse(validatorService.isIncludedIn(value, templateSubmitterService.getAllowedFormats()));
    }

    @Test
    void isIncludedIn_valueIsInArrayUsingRealConstraint_ShouldReturnTrue() {
        String value = "webp";
        TemplateFormValidatorService templateSubmitterService = new TemplateFormValidatorService(validatorService);
        assertTrue(validatorService.isIncludedIn(value, templateSubmitterService.getAllowedFormats()));
    }

    /* ***** isConstraintValidated ***** */

    @Test
    void isConstraintValidated_constraintIsNotValidated_ShouldReturnValidationResponseFalseNullErrorMessage() {
        int value = 479;
        InputConstraint testConstraint = new InputConstraint(
            "testConstraint",
            ConstraintType.GREATER_THAN,
            480,
            "test has failed"
            );
        ValidationResponse testResult = validatorService.isConstraintValidated(value, testConstraint);
        assertFalse(testResult.getIsSuccess());
        assertNull(testResult.getData());
        assertEquals(testResult.getMessage(), "test has failed");
    }

    @Test
    void isConstraintValidated_constraintIsValidated_ShouldReturnValidationResponseTrueNullNull() {
        int value = 480;
        InputConstraint testConstraint = new InputConstraint(
            "testConstraint",
            ConstraintType.GREATER_THAN,
            480,
            "test has passed"
            );
        ValidationResponse testResult = validatorService.isConstraintValidated(value, testConstraint);
        assertTrue(testResult.getIsSuccess());
        assertNull(testResult.getData());
        assertNull(testResult.getMessage());
    }
}
