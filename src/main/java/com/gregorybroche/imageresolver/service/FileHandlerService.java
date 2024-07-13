package com.gregorybroche.imageresolver.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import org.springframework.stereotype.Service;

@Service
public class FileHandlerService {
    private final Path resolverTempPath;
    private final String tempFolderName = "87E3DD71-5CB4-4B19-A0C5-505D20280114";
    public FileHandlerService(){
        String userTempDirectoryPath = System.getProperty("java.io.tmpdir");
        this.resolverTempPath = Paths.get(userTempDirectoryPath, tempFolderName);
    }

    public Path saveFileToTemp(File sourceFile) throws IOException{
        System.out.println("temp path = "+this.resolverTempPath);
        if(!Files.exists(this.resolverTempPath)){
            System.out.println("create dir");
            this.createTempFolder();
        }
        String tempFileName = "temp-"+sourceFile.getName();
        Path copyFilePath = this.resolverTempPath.resolve(tempFileName);
        Path savedFilePath = Files.copy(sourceFile.toPath(), copyFilePath, StandardCopyOption.REPLACE_EXISTING);
        return savedFilePath;
    }

    private void createTempFolder(){
        try {
            Files.createDirectory(this.resolverTempPath);
        } catch (FileAlreadyExistsException e) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        
    }
}
