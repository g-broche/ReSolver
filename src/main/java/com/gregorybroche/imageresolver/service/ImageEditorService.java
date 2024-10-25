package com.gregorybroche.imageresolver.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.gregorybroche.imageresolver.classes.ImageTemplate;

@Service
public class ImageEditorService {
    private ValidatorService validatorService;
    private UserDialogService userDialogService;
    public ImageEditorService(ValidatorService validatorService, UserDialogService userDialogService){
        this.validatorService = validatorService;
        this.userDialogService = userDialogService;
    }

    /**
     * Gets content of an image file if the file is supported
     * @param imageFile source image file
     * @return BufferedImage content or null if error
     */
    public BufferedImage getImageContentFromFile(File imageFile){
        if(!validatorService.isFileValidImageFormat(imageFile)){
            return null;
        }
        try {
            BufferedImage imageContent = ImageIO.read(imageFile);
            return imageContent;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Takes an image content and creates a new image content based on it and defined template to apply
     * @param baseImageContent buffered image content of a source image
     * @param templateParameters parameters as instance of ImageTemplate
     * @return new buffered image content based on inputs given
     */
    public BufferedImage editImage(BufferedImage baseImageContent, ImageTemplate templateParameters, Integer finalBufferedImageColorType){
        int targetWidth = templateParameters.getWidth();
        int targetHeight = templateParameters.getHeight();
        Image editedImage = baseImageContent.getScaledInstance(targetWidth, targetHeight, Image.SCALE_AREA_AVERAGING);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, finalBufferedImageColorType);
        outputImage.getGraphics().drawImage(editedImage, 0, 0, null);
        return outputImage;
    }

    /**
     * creates a basic BufferedImage object to use for testing purpose
     */
    public BufferedImage createTestImageContent() {
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                bufferedImage.setRGB(x, y, (x * y) % 256);
            }
        }
        return bufferedImage;
    }

    /*
     * Image format specific methods to further specify writer parameters if required
     */

    public void createJPGImage(BufferedImage bufferedImageContent, File imageFile) throws IOException{
        Boolean result = ImageIO.write(bufferedImageContent, "jpg", imageFile);
        if (!result){
            throw new IOException("Could not save image with name \""+imageFile.getName()+"\"");
        }
    }
    public void createJPEGImage(BufferedImage bufferedImageContent, File imageFile) throws IOException{
        Boolean result = ImageIO.write(bufferedImageContent, "jpeg", imageFile);
        if (!result){
            throw new IOException("Could not save image with name \""+imageFile.getName()+"\"");
        }
    }
    public void createPNGImage(BufferedImage bufferedImageContent, File imageFile) throws IOException{
        Boolean result = ImageIO.write(bufferedImageContent, "png", imageFile);
        if (!result){
            throw new IOException("Could not save image with name \""+imageFile.getName()+"\"");
        }
    }
    public void createBMPImage(BufferedImage bufferedImageContent, File imageFile) throws IOException{
        Boolean result = ImageIO.write(bufferedImageContent, "bmp", imageFile);
        if (!result){
            throw new IOException("Could not save image with name \""+imageFile.getName()+"\"");
        }
    }
    public void createWEBPImage(BufferedImage bufferedImageContent, File imageFile) throws IOException{
        Boolean result = ImageIO.write(bufferedImageContent, "webp", imageFile);
        if (!result){
            throw new IOException("Could not save image with name \""+imageFile.getName()+"\"");
        }
    }
    public void createImage(BufferedImage bufferedImageContent, String format, File imageFile) throws IOException{
        Boolean result = ImageIO.write(bufferedImageContent, format, imageFile);
        if (!result){
            throw new IOException("Could not save image with name \""+imageFile.getName()+"\"");
        }
    }
}
