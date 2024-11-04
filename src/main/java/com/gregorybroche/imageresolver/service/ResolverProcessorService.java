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
    private MetadataService metadataService;

    public ResolverProcessorService(
        ImageEditorService imageEditorService,
        FileHandlerService fileHandlerService,
        ValidatorService validatorService,
        MetadataService metadataService
        ){
        this.validatorService = validatorService;
        this.fileHandlerService = fileHandlerService;
        this.imageEditorService = imageEditorService;
        this.metadataService = metadataService;
    }

    /**
     * Method to handle the process of resizing the source image and then saving the result in a new image file based on a given template 
     * @param sourceImageContent BufferedImage content of the source image
     * @param imageTemplate Instance of ImageTemplate qui specification for the edited image to create
     * @param saveToDirectory Path instance of the directory where the edit image must be saved
     * @return File instance representing the newly created file or null if method reached its end without creating a file
     * @throws IOException
     */
    public File processImageForTemplate(BufferedImage sourceImageContent, ImageTemplate imageTemplate, Path saveToDirectory) throws IOException{
        if(sourceImageContent == null || imageTemplate == null || saveToDirectory == null){
            List<String> errorMessageComponents = new ArrayList<>();
            if (sourceImageContent == null) {
                errorMessageComponents.add("source image content is null");
            }
            if (imageTemplate == null) {
                errorMessageComponents.add("template is null");
            }
            if (saveToDirectory == null) {
                errorMessageComponents.add("save directory is null");
            }
            throw new IOException(String.join(", ", errorMessageComponents));
        }
        String templateExtension = imageTemplate.getFormat();
        if(!validatorService.isExtensionValid(templateExtension)){
            String errorMessage = "invalid format requested: requested "+templateExtension+" not in allowed formats ("+validatorService.getAllowedImageFormatsAsString()+")";
            throw new IOException(errorMessage);
        }

        File editedImageFile = fileHandlerService.setFileToSaveTo(imageTemplate, saveToDirectory);
        BufferedImage editedImageContent;
        
        switch (templateExtension) {
            case "jpg":
                editedImageContent = imageEditorService.editImage(sourceImageContent, imageTemplate, BufferedImage.TYPE_INT_RGB);
                imageEditorService.createJPGImage(editedImageContent, editedImageFile);
                if (metadataService.isMetadataLoaded()) {
                    metadataService.writeLoadedMetadataToJPEGFile(editedImageFile);
                }
                break;
        
            case "jpeg":
                editedImageContent = imageEditorService.editImage(sourceImageContent, imageTemplate, BufferedImage.TYPE_INT_RGB);
                imageEditorService.createJPEGImage(editedImageContent, editedImageFile);
                if (metadataService.isMetadataLoaded()) {
                    metadataService.writeLoadedMetadataToJPEGFile(editedImageFile);
                }
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
                errorMessageComponents.add("source image content is null");
            }
            if (templates == null) {
                errorMessageComponents.add("templates list is null");
            }
            if (templates != null && templates.size() == 0) {
                errorMessageComponents.add("templates list is empty");
            }
            if (saveToDirectory == null) {
                errorMessageComponents.add("save directory is null");
            }
            return new ValidationResponse(false, null, String.join(", ", errorMessageComponents));
        }
        List<File> createdFiles = new ArrayList<File>();
        try {   
            for (ImageTemplate template : templates) {
                File createdFile = processImageForTemplate(sourceImageContent, template, saveToDirectory);
                if(createdFile == null){
                    throw new Exception(
                        "aborted operation : could not save image for template '"+template.getTemplateName()+"' "+
                        "to directory : '"+saveToDirectory.toString()+"' "+
                        "with file name '"+template.getCompleteFileName()+"'"
                        );
                }
                createdFiles.add(createdFile);
            }
            return new ValidationResponse(true, null, null);
        } catch (Exception e) {
            for (File createdFile : createdFiles) {
                createdFile.delete();
            }
            return new ValidationResponse(false, null, "an error occured while processing and operation was aborted : "+e.getMessage());
        }
    }
}
