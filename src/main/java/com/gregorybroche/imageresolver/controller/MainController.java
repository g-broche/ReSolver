package com.gregorybroche.imageresolver.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.gregorybroche.imageresolver.classes.Preset;
import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.service.FileHandlerService;
import com.gregorybroche.imageresolver.service.ImageEditorService;
import com.gregorybroche.imageresolver.service.PresetManagementService;
import com.gregorybroche.imageresolver.service.ResolverProcessorService;
import com.gregorybroche.imageresolver.service.UserDialogService;
import com.gregorybroche.imageresolver.service.ValidatorService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
public class MainController {
    private ApplicationContext applicationContext;
    private UserDialogService userDialogService;
    private FileHandlerService fileHandlerService;
    private ValidatorService validatorService;
    private ImageEditorService imageEditorService;
    private ResolverProcessorService resolverProcessorService;
    PresetManagementService presetManagementService;
    private File imageToResolve = null;
    private String Selectedpreset = "test";

    public MainController(
            ApplicationContext applicationContext,
            UserDialogService userDialogService,
            FileHandlerService fileHandlerService,
            ValidatorService validatorService,
            ImageEditorService imageEditorService,
            ResolverProcessorService resolverProcessorService,
            PresetManagementService presetManagementService
            ) {
        this.applicationContext = applicationContext;
        this.userDialogService = userDialogService;
        this.fileHandlerService = fileHandlerService;
        this.validatorService = validatorService;
        this.imageEditorService = imageEditorService;
        this.resolverProcessorService = resolverProcessorService;
        this.presetManagementService = presetManagementService;
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
    public void initialize() {
        presetManagementService.loadPresets();
    }

    @FXML
    void selectImage(MouseEvent event) {
        try {
            File selectedFile = userDialogService.selectImageFile();
            if (!validatorService.isFileValidImageFormat(selectedFile)) {
                userDialogService.showInvalidSelectedFileFormatError();
                return;
            }
            imageToResolve = fileHandlerService.saveFileToTemp(selectedFile).toFile();
            Image previewImage = getImageFromFilePath(imageToResolve.toPath());
            displaySelectedImagePreview(previewImage);
        } catch (Exception e) {
            userDialogService.showErrorMessage("failed to select image", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    void resolveImage(MouseEvent event) {
        try {
            BufferedImage sourceBufferedImage = imageEditorService.getImageContentFromFile(imageToResolve);
            ImageTemplate template = getTemplateParameters();
            Path directory = fileHandlerService.getAppDirectoryPath(); // Temporary for testing until logic for defining
                                                                       // templates and presets through inputs is done
            resolverProcessorService.processImageForTemplate(sourceBufferedImage, template, directory);
        } catch (Exception e) {
            userDialogService.showErrorMessage("failed to resolve image", e.getMessage());
        }
    }

    @FXML
    void openTemplateForm(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/templateForm.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();

            TemplateFormController templateFormController = loader.getController();
            templateFormController.setFormSubmitListener(newTemplate -> {
                addSubmittedTemplateToPreset(newTemplate);
            }
            );

            Stage stage = new Stage();
            stage.setTitle("Template Form");

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.initModality(Modality.WINDOW_MODAL);

            stage.initOwner(((Node) event.getSource()).getScene().getWindow());

            stage.showAndWait();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Return an Image object generated based on a given file path
     * 
     * @param filePath instance of Path corresponding to the target file
     * @return Image object generated
     */
    private Image getImageFromFilePath(Path filePath) {
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
    private void displaySelectedImagePreview(Image image) {
        imagePreview.setImage(image);
    }

    // Proof of concept testing until proper definition of templates through user
    // inputs
    private ImageTemplate getTemplateParameters() {
        return new ImageTemplate("testTemplate", 600, 400, 96, null, "testImage", null, "jpg");
    }

    private void addSubmittedTemplateToPreset(ImageTemplate template) {
        System.out.println("submitted template : "+template.getTemplateName());
        presetManagementService.addTemplateToPreset(template, Selectedpreset);
        System.out.println("template was added; preset '"+presetManagementService.getPresetFromKey(Selectedpreset).getName()+
        "' -> template '"+presetManagementService.getPresetFromKey(Selectedpreset).getTemplates().get(0).getTemplateName()+"'");
    }
}
