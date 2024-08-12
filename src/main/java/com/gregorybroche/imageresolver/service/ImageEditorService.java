package com.gregorybroche.imageresolver.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.gregorybroche.imageresolver.classes.ImageTemplate;

@Service
public class ImageEditorService {
    /**
     * Takes an image content and creates a new image content based on it and defined template to apply
     * @param baseImageContent buffered image content of a source image
     * @param templateParameters parameters as instance of ImageTemplate
     * @return new buffered image content based on inputs given
     */
    public BufferedImage editImage(BufferedImage baseImageContent, ImageTemplate templateParameters){
        int targetWidth = templateParameters.getWidth();
        int targetHeight = templateParameters.getHeight();
        BufferedImage editedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2d = editedImage.createGraphics();
        graphics2d.drawImage(baseImageContent, 0, 0, targetWidth, targetHeight, null);
        graphics2d.dispose();
        return editedImage;
    }

    public BufferedImage createTestImageContent() {
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                bufferedImage.setRGB(x, y, (x * y) % 256);
            }
        }
        return bufferedImage;
    }

    public void createImage(File imageFile, String format, BufferedImage bufferedImageContent) throws IOException{
        ImageIO.write(bufferedImageContent, format, imageFile);
    }

    public void createJPGImage(File imageFile, BufferedImage bufferedImageContent) throws IOException{
        ImageIO.write(bufferedImageContent, "jpg", imageFile);
    }
    public void createJPEGImage(File imageFile, BufferedImage bufferedImageContent) throws IOException{
        ImageIO.write(bufferedImageContent, "jpeg", imageFile);
    }
    public void createPNGImage(File imageFile, BufferedImage bufferedImageContent) throws IOException{
        ImageIO.write(bufferedImageContent, "png", imageFile);
    }
    public void createBMPImage(File imageFile, BufferedImage bufferedImageContent) throws IOException{
        ImageIO.write(bufferedImageContent, "bmp", imageFile);
    }
    public void createWEBPImage(File imageFile, BufferedImage bufferedImageContent) throws IOException{
        ImageIO.write(bufferedImageContent, "webp", imageFile);
    }
}
