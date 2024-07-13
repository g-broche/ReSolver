package com.gregorybroche.imageresolver.controller;

import java.io.File;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.gregorybroche.imageresolver.service.FileHandlerService;
import com.gregorybroche.imageresolver.service.UserDialogService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

@Component
public class MainController {
    private final UserDialogService userDialogService;
    private final FileHandlerService fileHandlerService;

    public MainController(UserDialogService userDialogService, FileHandlerService fileHandlerService) {
        this.userDialogService = userDialogService;
        this.fileHandlerService = fileHandlerService;
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
            Path savedFilePath = fileHandlerService.saveFileToTemp(selectedFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void resolveImage(MouseEvent event) {

    }

}
