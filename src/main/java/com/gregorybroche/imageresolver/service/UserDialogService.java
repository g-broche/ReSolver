package com.gregorybroche.imageresolver.service;

import java.io.File;

import org.springframework.stereotype.Service;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

@Service
public final class UserDialogService {

    public File selectImageFile(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        return selectedFile;
    }
}
