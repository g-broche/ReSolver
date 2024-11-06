package com.gregorybroche.imageresolver.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.gregorybroche.imageresolver.classes.ImageTemplate;
import com.gregorybroche.imageresolver.classes.Preset;
import com.gregorybroche.imageresolver.classes.ValidationResponse;

@Service
public class FileHandlerService {
    private ApplicationContext applicationContext;
    private UserDialogService userDialogService;
    private XmlService xmlService;
    private final Path appDirectoryPath;
    private final Path appTempDirectoryPath;
    private final String appPresetsFileName;
    private final Path appPresetsFilePath;

    public FileHandlerService(ApplicationContext applicationContext,
            UserDialogService userDialogService,
            XmlService xmlService,
            @Value("${resolver.directory.main}") String mainDirectoryName,
            @Value("${resolver.directory.temp}") String tempDirectoryName,
            @Value("${resolver.file.presets}") String presetFileName,
            @Value("${resolver.directory.windows}") String windowsAppMainDirectoryRoot,
            @Value("${resolver.directory.unix}") String unixAppMainDirectoryRoot) {
        this.applicationContext = applicationContext;
        this.userDialogService = userDialogService;
        this.xmlService = xmlService;
        String userTempDirectoryPath = System.getProperty("java.io.tmpdir");
        String userHomeDirectoryPath = System.getProperty("user.home");

        // defining system specific paths to application folder containing the xml
        // preset file
        String osFileAccessorPath;
        String userOperatingSystem = System.getProperty("os.name").toLowerCase();
        if (userOperatingSystem.contains("win")) {
            osFileAccessorPath = windowsAppMainDirectoryRoot;
        } else if (userOperatingSystem.contains("lin")) {
            osFileAccessorPath = unixAppMainDirectoryRoot;
        } else {
            throw new RuntimeException("This software is not supported on your operating system");
        }

        this.appTempDirectoryPath = Paths.get(userTempDirectoryPath, tempDirectoryName);
        this.appDirectoryPath = Paths.get(userHomeDirectoryPath, osFileAccessorPath, mainDirectoryName);
        this.appPresetsFilePath = Paths.get(userHomeDirectoryPath, osFileAccessorPath, mainDirectoryName,
                presetFileName);
        this.appPresetsFileName = presetFileName;
    }

    public Path getAppDirectoryPath() {
        return this.appDirectoryPath;
    }

    public Path getAppTempDirectoryPath() {
        return this.appTempDirectoryPath;
    }

    /**
     * Creates copy of a file into the temp folder, will create temp folder if not
     * already present.
     * 
     * @param sourceFile
     * @return path of saved file
     * @throws IOException
     */
    public Path saveFileToTemp(File sourceFile) throws IOException {
        if (!Files.exists(this.appTempDirectoryPath)) {
            this.createTempFolder();
        }
        String tempFileName = "temp-" + sourceFile.getName();
        Path copyFilePath = this.appTempDirectoryPath.resolve(tempFileName);
        Path savedFilePath = Files.copy(sourceFile.toPath(), copyFilePath, StandardCopyOption.REPLACE_EXISTING);
        return savedFilePath;
    }

    /**
     * create an instance of file representing where an edited image will be saved,
     * generate directory if does not already exist
     * 
     * @param imageTemplate   image template instance for the file name
     *                        specification
     * @param targetDirectory directory where the image must be saved
     * @return File object representing the future file holding the processed image
     * @throws IOException
     */
    public File setFileToSaveTo(ImageTemplate imageTemplate, Path targetDirectory) throws IOException {
        if (!Files.exists(targetDirectory)) {
            this.createFolder(targetDirectory);
        }
        String newImageFullName = imageTemplate.getCompleteFileName();
        File toSaveFile = targetDirectory.resolve(newImageFullName).toFile();
        return toSaveFile;
    }

    /**
     * create folder based on given Path instance
     * 
     * @param directory Path instance of the directory to create
     */
    public void createFolder(Path directory) {
        try {
            Files.createDirectory(directory);
        } catch (FileAlreadyExistsException e) {
            return;
        } catch (IOException e) {
            userDialogService.showErrorMessage("Failed directory creation",
                    "Could not create \"" + directory + "\" ; Error : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * saves a list of preset in a xml file at the location specificed by the application environment
     * @param presets presets to save
     * @return ValidationResponse instance with success state of the operation
     */
    public ValidationResponse savePresets(List<Preset> presets){
        try {
            if(!Files.exists(appDirectoryPath)){
                createAppFolder();
            }
            if(presets == null || presets.isEmpty()){
                return new ValidationResponse(false, null, "No currently managed presets");
            }
            Path pathToSaveTo = appDirectoryPath.resolve(appPresetsFileName);
            Document presetListDocument = xmlService.createPresetsXMLDocument(presets);
            return xmlService.writeXML(presetListDocument, pathToSaveTo);
        } catch (Exception e) {
            return new ValidationResponse(false, null, "could not save presets to file, error : "+e.getMessage());
        }
    }

    /**
     * creates the folder hosting permanent application file (like saved presets and
     * directory for resolved images)
     */
    public void createAppFolder() {
        createFolder(this.appDirectoryPath);
    }

    /**
     * creates temp folder used to hold temp data like a copy of the image to be
     * processed
     */
    public void createTempFolder() {
        createFolder(this.appTempDirectoryPath);
    }
}
