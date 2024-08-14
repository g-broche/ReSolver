package com.gregorybroche.imageresolver.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.gregorybroche.imageresolver.classes.ImageTemplate;

@Service
public class ResolverProcessorService {
    private ImageEditorService imageEditorService;
    private FileHandlerService fileHandlerService;
    private ValidatorService validatorService;

    public ResolverProcessorService(ImageEditorService imageEditorService, FileHandlerService fileHandlerService, ValidatorService validatorService){
        this.validatorService = validatorService;
        this.fileHandlerService = fileHandlerService;
        this.imageEditorService = imageEditorService;
    }

    /**
     * Method to handle the process of resizing the source image and then saving the result in a new image file based on a given template 
     * @param sourceImageContent BufferedImage content of the source image
     * @param imageTemplate Instance of ImageTemplate qui specification for the edited image to create
     * @param saveToDirectory Path instance of the directory where the edit image must be saved
     * @throws IOException
     */
    public void processImageForTemplate(BufferedImage sourceImageContent, ImageTemplate imageTemplate, Path saveToDirectory) throws IOException{

        String templateExtension = imageTemplate.getFormat();
        if(!validatorService.isExtensionValid(templateExtension)){
            String errorMessage = "Invalid format requested: requested "+templateExtension+" not in allowed formats ("+validatorService.getAllowedImageFormatsAsString()+")";
            throw new IOException(errorMessage);
        }

        File editedImageFile = fileHandlerService.setFileToSaveTo(imageTemplate, saveToDirectory);
        BufferedImage editedImageContent;
        
        switch (templateExtension) {
            case "jpg":
                editedImageContent = imageEditorService.editImage(sourceImageContent, imageTemplate, BufferedImage.TYPE_INT_RGB);
                imageEditorService.createJPGImage(editedImageContent, editedImageFile);
                break;
        
            case "jpeg":
                editedImageContent = imageEditorService.editImage(sourceImageContent, imageTemplate, BufferedImage.TYPE_INT_RGB);
                imageEditorService.createJPEGImage(editedImageContent, editedImageFile);
                break;
                
            case "bmp":
                editedImageContent = imageEditorService.editImage(sourceImageContent, imageTemplate, BufferedImage.TYPE_INT_RGB);
                imageEditorService.createBMPImage(editedImageContent, editedImageFile);
                break;

            case "png":
                editedImageContent = imageEditorService.editImage(sourceImageContent, imageTemplate, BufferedImage.TYPE_INT_ARGB);
                imageEditorService.createPNGImage(editedImageContent, editedImageFile);
                break;

        
            case "webp":
                editedImageContent = imageEditorService.editImage(sourceImageContent, imageTemplate, BufferedImage.TYPE_INT_ARGB);
                imageEditorService.createWEBPImage(editedImageContent, editedImageFile);
                break;
        
            default:
                editedImageContent = imageEditorService.editImage(sourceImageContent, imageTemplate, BufferedImage.TYPE_INT_RGB);
                imageEditorService.createImage(editedImageContent, templateExtension, editedImageFile);
                break;
        }
    }
}
