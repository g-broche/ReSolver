package com.gregorybroche.imageresolver.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.service.FileHandlerService;
import com.gregorybroche.imageresolver.service.ImageEditorService;
import com.gregorybroche.imageresolver.service.ResolverProcessorService;
import com.gregorybroche.imageresolver.service.UserDialogService;
import com.gregorybroche.imageresolver.service.ValidatorService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

@Component
public class MainController {
    private UserDialogService userDialogService;
    private FileHandlerService fileHandlerService;
    private ValidatorService validatorService;
    private ImageEditorService imageEditorService;
    private ResolverProcessorService resolverProcessorService;
    private File imageToResolve = null;

    public MainController(  UserDialogService userDialogService,
                            FileHandlerService fileHandlerService,
                            ValidatorService validatorService,
                            ImageEditorService imageEditorService,
                            ResolverProcessorService resolverProcessorService) {
        this.userDialogService = userDialogService;
        this.fileHandlerService = fileHandlerService;
        this.validatorService = validatorService;
        this.imageEditorService = imageEditorService;
        this.resolverProcessorService = resolverProcessorService;
    } 
    
    @FXML
    private ImageView imagePreview;

    @FXML
    private Button imageResolverButton;

    @FXML
    private Button imageSelectorButton;

    @FXML
    private Button createTemplateButton;

    @FXML
    private VBox templateContainer;

    @FXML
    void selectImage(MouseEvent event) {
        try {
            File selectedFile = userDialogService.selectImageFile();
            if (!validatorService.isFileValidImageFormat(selectedFile)) {
                this.userDialogService.showInvalidSelectedFileFormatError();
                return;
            }
            this.imageToResolve = fileHandlerService.saveFileToTemp(selectedFile).toFile();
            Image previewImage = getImageFromFilePath(imageToResolve.toPath());
            this.displaySelectedImagePreview(previewImage);
        } catch (Exception e) {
            userDialogService.showErrorMessage("failed to select image", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    void resolveImage(MouseEvent event) {
        try{
            BufferedImage sourceBufferedImage = imageEditorService.getImageContentFromFile(imageToResolve);
            ImageTemplate template = getTemplateParameters();
            Path directory = fileHandlerService.getAppDirectoryPath(); //Temporary for testing until logic for defining templates and presets through inputs is done
            resolverProcessorService.processImageForTemplate(sourceBufferedImage, template, directory);
        } catch (Exception e) {
            userDialogService.showErrorMessage("failed to resolve image", e.getMessage());
        }
    }

    @FXML
    /**
     * Implementation as proof of concept of creating an ImageTemplate instance and displaying the
     * corresponding data into an appended fxml component
     * @param event
     */
    void createTemplate(MouseEvent event){
        System.out.println("***CREATING TEMPLATE VIEW***");
        System.out.println(templateContainer);
        // Create a new ImageTemplate instance
        ImageTemplate newTemplate = new ImageTemplate(null, 600, 400, null, null, null, null, "jpg");
        System.out.println("***CREATING IMAGE TEMPLATE INSTANCE***");

        // Create the template pane (an HBox) using the FXML and add it to the VBox
        templateContainer.getChildren().add(newTemplate.createTemplatePane(0));
        System.out.println("***DISPLAY TEMPLATE***");
    }

    /**
     * Return an Image object generated based on a given file path
     * @param filePath instance of Path corresponding to the target file
     * @return Image object generated
     */
    private Image getImageFromFilePath(Path filePath){
        try {
            FileInputStream inputStream = new FileInputStream(filePath.toFile());
            Image image = new Image(inputStream);
            return image;
        } catch (Exception e) {
            System.err.println("Error : unable to generate image element from the selected file");
            throw new RuntimeException(e);
        }
    }

    /**
     * Display preview of currently selected image
     */
    private void displaySelectedImagePreview(Image image){
        imagePreview.setImage(image);
    }

    // Proof of concept testing until proper definition of templates through user inputs
    private ImageTemplate getTemplateParameters(){
        return new ImageTemplate(null, 600, 400, null, null, null, null, "jpg");
    }

}
