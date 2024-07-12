package com.gregorybroche.imageresolver.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import org.springframework.stereotype.Service;

@Service
public class FileHandlerService {
    private final String tempDir = "/com/gregorybroche/imageresolver/images/temp";
    private final Path tempPath = Paths.get("/com/gregorybroche/imageresolver/images/temp");

    public Path saveFileToTemp(File sourceFile) throws IOException{
        if(!Files.exists(this.tempPath)){
            this.createTempFolder();
        }
        String tempFileName = "temp-"+sourceFile.getName();
        Path copyFilePath = this.tempPath.resolve(tempFileName);
        Path savedFilePath = Files.copy(sourceFile.toPath(), copyFilePath, StandardCopyOption.REPLACE_EXISTING);
        return savedFilePath;
    }

    private void createTempFolder(){
        try {
            Files.createDirectory(this.tempPath);
        } catch (FileAlreadyExistsException e) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        
    }
}
