package com.gregorybroche.imageresolver.service;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class ValidatorService {
    private final List<String> allowedImageFormats = Arrays.asList("*.jpg", "*.jpeg", "*.png", "*.bmp", "*.webp");
    private final List<String> allowedImageMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/bmp", "image/webp");

    /**
     * Validate that a file mime type complies with allowed mime types. 
     * @param file file to validate
     * @return true if file complies, false otherwise
     */
    public Boolean isFileValidImageFormat(File file) {
        try {
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null || !this.allowedImageMimeTypes.contains(mimeType)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isExtensionValid(String extension){
        return getAllowedImageFormatsAsExtension().contains(extension);
    }

    /**
     * return List of allowed image formats with syntax like "*.webp"
     * @return
     */
    public List<String> getAllowedImageFormats(){
        return this.allowedImageFormats;
    }
    /**
     * return List of allowed image extension with syntax like "webp"
     * @return
     */
    public Set<String> getAllowedImageFormatsAsExtension(){
        Set<String> allowedExtensions = new HashSet<String>();
        for (String format : allowedImageFormats) {
            allowedExtensions.add(format.replace("*.", ""));
        }
        return allowedExtensions;
    }

    /**
     * return stringified list of allowed image formats
     * @return
     */
    public String getAllowedImageFormatsAsString(){
        String stringifiedAllowedFormat = String.join(", ", this.allowedImageFormats);
        return stringifiedAllowedFormat.replace("*", "");
    }
}
