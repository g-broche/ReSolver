package com.gregorybroche.imageresolver.service;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

@Service
public final class UserDialogService {
    private final ValidatorService validatorService;

    public UserDialogService(ValidatorService validatorService){
        this.validatorService = validatorService;
    }

    /**
     * Open a dialogue for the user to select an image from storage
     * @return selected file
     */
    public File selectImageFile(){
        FileChooser fileChooser = this.generateImageFileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        return selectedFile;
    }

    /**
     * Creates a FileChooser instance and configure it for image format only
     * @return configured FileChooser instance
     */
    private FileChooser generateImageFileChooser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select source image");
        List<String> allowedFormats = this.validatorService.getAllowedImageFormats();
        ExtensionFilter fileChooserFilter = new ExtensionFilter("Image Files", allowedFormats);
        fileChooser.getExtensionFilters().add(fileChooserFilter);
        return fileChooser;
    }

    /**
     * Display error popup
     * @param title
     * @param message
     */
    public void showErrorMessage(String title, String message){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public void showInvalidSelectedFileFormatError(){
        String title = "File is not valid";
        String message = "The file must be an image with one of the following type: " + validatorService.getAllowedImageFormatsAsString();
        this.showErrorMessage(title, message);
    }
}
