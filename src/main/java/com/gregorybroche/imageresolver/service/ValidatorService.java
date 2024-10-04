package com.gregorybroche.imageresolver.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import com.gregorybroche.imageresolver.Enums.ConstraintType;

@Service
public class ValidatorService {
    private final List<String> allowedImageFormats = Arrays.asList("*.jpg", "*.jpeg", "*.png", "*.bmp", "*.webp",
            "*.avif");
    private final List<String> allowedImageMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/bmp",
            "image/webp", "image/avif");

    /**
     * Validate that a file mime type complies with allowed mime types.
     * 
     * @param file file to validate
     * @return true if file complies, false otherwise
     */
    public Boolean isFileValidImageFormat(File file) {
        try {
            String mimeType = getFileMimeType(file);
            if (mimeType == null || !this.allowedImageMimeTypes.contains(mimeType)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getFileMimeType(File file) throws IOException {
        Tika tika = new Tika();
        return tika.detect(file);
    }

    public Boolean isExtensionValid(String extension) {
        return getAllowedImageFormatsAsExtension().contains(extension);
    }

    /**
     * return List of allowed image formats with syntax like "*.webp"
     * 
     * @return
     */
    public List<String> getAllowedImageFormats() {
        return this.allowedImageFormats;
    }

    /**
     * return List of allowed image extension with syntax like "webp"
     * 
     * @return
     */
    public Set<String> getAllowedImageFormatsAsExtension() {
        Set<String> allowedExtensions = new HashSet<String>();
        for (String format : allowedImageFormats) {
            allowedExtensions.add(format.replace("*.", ""));
        }
        return allowedExtensions;
    }

    /**
     * returns stringified list of allowed image formats
     * 
     * @return
     */
    public String getAllowedImageFormatsAsString() {
        String stringifiedAllowedFormat = String.join(", ", this.allowedImageFormats);
        return stringifiedAllowedFormat.replace("*", "");
    }

    /**
     * Validates if an input complies with a constraint
     * @param input Input to validate, accepts Object to cover all case
     * @param constraintValue Value of the constraint, accepts Object to cover all case
     * @param constraintType Type of the constraint based on an enum
     * @return True if input is compliant with the constraint
     */
    public boolean isConstraintValidated(Object input, Object constraintValue, ConstraintType constraintType) {
        boolean isConstraintValid = false;
        switch (constraintType) {
            case REQUIRED:
                isConstraintValid = isNotEmpty(input);
                break;

            case GREATER_THAN:
                isConstraintValid = isGreaterThan(input, (Integer) constraintValue);
                break;

            case LESS_THAN:
                isConstraintValid = isLessThan(input, (Integer) constraintValue);
                break;

            case LONGER_THAN:
                isConstraintValid = isLongerThan(input, (Integer) constraintValue);
                break;

            case SHORTER_THAN:
                isConstraintValid = isShorterThan(input, (Integer) constraintValue);
                break;

            default:
                break;
        }
        return isConstraintValid;
    }

    /**
     * Validates if an input is one allowed for string comparisons (string & integer)
     * @param input
     * @return True if validated
     */
    public boolean isValueStringCompatible(Object input) {
        return (input instanceof Integer) || (input instanceof String);
    }

    /**
     * Validates if an input is not considered empty (null & empty string)
     * @param input
     * @return True if input is not empty
     */
    public boolean isNotEmpty(Object input) {
        return input != null && input.toString() != "";
    }

    /**
     * Validates if an input has a greater numerical value than the minimum allowed
     * @param input
     * @return True if input is greater than the minimum
     */
    public boolean isGreaterThan(Object input, Integer min) {
        if (!(input instanceof Integer)) {
            return false;
        }
        return (Integer) input >= min;
    }

    /**
     * Validates if an input has a lower numerical value than the maximum allowed
     * @param input
     * @return True if input is lower than the minimum
     */
    public boolean isLessThan(Object input, Integer max) {
        if (!(input instanceof Integer)) {
            return false;
        }
        return (Integer) input <= max;
    }

    /**
     * Validates if an input has more characters than the minimum allowed
     * @param input
     * @return True if input is longer than the minimum
     */
    public boolean isLongerThan(Object input, Integer minLen) {
        if (!isValueStringCompatible(input)) {
            return false;
        }
        return input.toString().length() >= minLen;
    }

    /**
     * Validates if an input has less characters than the maximum allowed
     * @param input
     * @return True if input is short than the maximum
     */
    public boolean isShorterThan(Object input, Integer maxLen) {
        if (!isValueStringCompatible(input)) {
            return false;
        }
        return input.toString().length() <= maxLen;
    }

    // public boolean isIncludedIn(Object input, Object[] allowedValues) {
    //     for (Object allowedValue : allowedValues) {
    //         if (input.equals(allowedValue)) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }
}
