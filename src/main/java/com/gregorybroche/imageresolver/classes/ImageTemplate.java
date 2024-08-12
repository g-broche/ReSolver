package com.gregorybroche.imageresolver.classes;

public class ImageTemplate {
    private int width = 600;
    private int height = 400;
    private String newImageName = "test-editing";
    private String format = "jpg";

    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() {
        return width;
    }

    public void setNewImageName(String newImageName) {
        this.newImageName = newImageName;
    }
    public String getNewImageName() {
        return newImageName;
    }

    public void setFormat(String format) {
        this.format = format;
    }
    public String getFormat() {
        return format;
    }
}
