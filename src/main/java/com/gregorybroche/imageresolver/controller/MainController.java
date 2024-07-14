package com.gregorybroche.imageresolver.controller;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.gregorybroche.imageresolver.service.FileHandlerService;
import com.gregorybroche.imageresolver.service.UserDialogService;
import com.gregorybroche.imageresolver.service.ValidatorService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

@Component
public class MainController {
    private final UserDialogService userDialogService;
    private final FileHandlerService fileHandlerService;
    private final ValidatorService validatorService;
    private Image imageToResolve = null;

    public MainController(UserDialogService userDialogService, FileHandlerService fileHandlerService, ValidatorService validatorService) {
        this.userDialogService = userDialogService;
        this.fileHandlerService = fileHandlerService;
        this.validatorService = validatorService;
    } 
    
    @FXML
    private ImageView imagePreview;

    @FXML
    private Button imageResolverButton;

    @FXML
    private Button imageSelectorButton;

    @FXML
    void selectImage(MouseEvent event) {
        try {
            File selectedFile = userDialogService.selectImageFile();
            if (!validatorService.isFileValidImageFormat(selectedFile)) {
                this.userDialogService.showInvalidSelectedFileFormatError();
                return;
            }
            Path savedFilePath = fileHandlerService.saveFileToTemp(selectedFile);
            this.imageToResolve = this.getImageFromFilePath(savedFilePath);
            this.displaySelectedImagePreview(this.imageToResolve);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void resolveImage(MouseEvent event) {

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

}
