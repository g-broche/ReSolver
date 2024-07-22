package com.gregorybroche.imageresolver.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class FileHandlerService {
    private final ApplicationContext applicationContext;
    private final Path appDirectoryPath;
    private final Path appTempDirectoryPath;
    private final String appPresetsFileName;
    private final Path appPresetsFilePath;
    
    public FileHandlerService(  ApplicationContext applicationContext,
                                @Value("${resolver.directory.main}") String mainDirectoryName,
                                @Value("${resolver.directory.temp}") String tempDirectoryName,
                                @Value("${resolver.file.presets}") String presetFileName,
                                @Value("${resolver.directory.windows}") String windowsAppMainDirectoryRoot,
                                @Value("${resolver.directory.unix}") String unixAppMainDirectoryRoot){
        this.applicationContext = applicationContext;
        String userTempDirectoryPath = System.getProperty("java.io.tmpdir");
        String userHomeDirectoryPath = System.getProperty("user.home");

        //defining system specific paths to application folder containing the xml preset file
        String osFileAccessorPath;
        String userOperatingSystem = System.getProperty("os.name").toLowerCase();
        if(userOperatingSystem.contains("win")){
            osFileAccessorPath = windowsAppMainDirectoryRoot;
        }else if (userOperatingSystem.contains("lin")){
            osFileAccessorPath = unixAppMainDirectoryRoot;
        }else{
            throw new RuntimeException("This software is not supported on your operating system");
        }

        this.appTempDirectoryPath = Paths.get(userTempDirectoryPath, tempDirectoryName);
        this.appDirectoryPath = Paths.get(userHomeDirectoryPath, osFileAccessorPath, mainDirectoryName);
        this.appPresetsFilePath = Paths.get(userHomeDirectoryPath, osFileAccessorPath, mainDirectoryName, presetFileName);
        this.appPresetsFileName = presetFileName;
    }

    public Path getAppDirectoryPath(){
        return this.appDirectoryPath;
    }
    public Path getAppTempDirectoryPath(){
        return this.appTempDirectoryPath;
    }

    /**
     * Creates copy of a file into the temp folder, will create temp folder if not already present.
     * @param sourceFile
     * @return path of saved file
     * @throws IOException
     */
    public Path saveFileToTemp(File sourceFile) throws IOException{
        if(!Files.exists(this.appTempDirectoryPath)){
            this.createTempFolder();
        }
        String tempFileName = "temp-"+sourceFile.getName();
        Path copyFilePath = this.appTempDirectoryPath.resolve(tempFileName);
        Path savedFilePath = Files.copy(sourceFile.toPath(), copyFilePath, StandardCopyOption.REPLACE_EXISTING);
        return savedFilePath;
    }

    public void createAppFolder(){
        try {
            Files.createDirectory(this.appDirectoryPath);
        } catch (FileAlreadyExistsException e) {
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void createTempFolder(){
        try {
            Files.createDirectory(this.appTempDirectoryPath);
        } catch (FileAlreadyExistsException e) {
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
