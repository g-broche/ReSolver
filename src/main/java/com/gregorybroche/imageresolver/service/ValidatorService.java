package com.gregorybroche.imageresolver.service;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ValidatorService {
    private final List<String> allowedImageFormats = Arrays.asList("*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp", "*.webp");
    private final List<String> allowedImageMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp");

    /**
     * Validate that a file mime type complies with allowed mime types. 
     * @param file file to validate
     * @return true if file complies, false otherwise
     */
    public Boolean isFileValidImageFormat(File file){
        try {
            String mimeType = Files.probeContentType(file.toPath());
            System.out.println("mime ="+mimeType);
            if (mimeType == null || !this.allowedImageMimeTypes.contains(mimeType)) {
                System.out.println("invalid image format");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error : Could not validate file compliance with allowed image formats");
            throw new RuntimeException(e);
        }
    }

    /**
     * return List of allowed image formats with syntax like *.webp
     * @return
     */
    public List<String> getAllowedImageFormats(){
        return this.allowedImageFormats;
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
