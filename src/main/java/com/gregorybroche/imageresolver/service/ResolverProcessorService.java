package com.gregorybroche.imageresolver.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.classes.ValidationResponse;

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
    public File processImageForTemplate(BufferedImage sourceImageContent, ImageTemplate imageTemplate, Path saveToDirectory) throws IOException{

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
        if(!Files.exists(editedImageFile.toPath())){
            return null;
        }
        return editedImageFile;
    }

    /**
     * creates the image copies of a source image after applying templates' specification and saving to defined directory
     * @param sourceImageContent
     * @param templates
     * @param saveToDirectory
     * @return ValidationResponse instance describing the success state of the operation
     */
    public ValidationResponse resolveImageForAllTemplates(BufferedImage sourceImageContent, List<ImageTemplate> templates, Path saveToDirectory){
        if(sourceImageContent == null || templates == null || saveToDirectory == null || templates.size()==0){
            List<String> errorMessageComponents = new ArrayList<>();
            if (sourceImageContent == null) {
                errorMessageComponents.add("Source image content is missing");
            }
            if (templates == null) {
                errorMessageComponents.add("Templates list is missing");
            }
            if (templates != null && templates.size() == 0) {
                errorMessageComponents.add("Templates list is empty");
            }
            if (saveToDirectory == null) {
                errorMessageComponents.add("Save directory is missing");
            }
            return new ValidationResponse(false, null, String.join(", ", errorMessageComponents));
        }
        List<File> createdFiles = new ArrayList<File>();
        try {   
            for (ImageTemplate template : templates) {
                File createdFile = processImageForTemplate(sourceImageContent, template, saveToDirectory);
                if(createdFile == null){
                    throw new Exception(
                        "Could not save image for template '"+template.getTemplateName()+"'"+
                        " to directory : '"+saveToDirectory.toString()+"'"+
                        " with file name '"+template.getCompleteFileName()+"'"
                        );
                }
                createdFiles.add(createdFile);
            }
            return new ValidationResponse(true, null, null);
        } catch (Exception e) {
            for (File createdFile : createdFiles) {
                createdFile.delete();
            }
            return new ValidationResponse(false, null, "An error occured while processing and saving images : "+e.getMessage());
        }
    }
}
