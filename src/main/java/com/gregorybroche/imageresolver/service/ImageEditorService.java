package com.gregorybroche.imageresolver.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

@Service
public class ImageEditorService {
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
