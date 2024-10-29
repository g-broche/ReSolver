package com.gregorybroche.imageresolver.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.springframework.stereotype.Service;

import com.gregorybroche.imageresolver.Enums.MetadataKey;
import com.gregorybroche.imageresolver.classes.MetadataItem;

@Service
public class MetadataService {
    private List<MetadataItem> metadataItems = new ArrayList<MetadataItem>();
    private Map<String, MetadataKey> exifKeysToMetadataKeys;

    public MetadataService(){
        this.exifKeysToMetadataKeys = MetadataKey.getExifKeysToMetadataKey();
    }

    public Map<String, MetadataKey> getExifKeysToMetadataKeys() {
        return exifKeysToMetadataKeys;
    }

    public ImageMetadata getMetadataFromFile(File file) throws ImagingException, IOException {
        ImageMetadata metadata = Imaging.getMetadata(file);
        return metadata;
    }

    public Map<String, String> ExtractMetadataToMap(ImageMetadata imageMetadata){
        if(imageMetadata == null){
            return null;
        }
        Map<String, String> mappedData = new HashMap<>();
        imageMetadata.getItems();
        return mappedData;
    }

    public void printMetadata(File file) throws ImagingException, IOException {
        ImageMetadata metadata = Imaging.getMetadata(file);
        System.out.println(metadata);
    }


    public void extractMetadata(File file) throws ImagingException, IOException{
        ImageMetadata metadata = getMetadataFromFile(file);
        if (metadata instanceof JpegImageMetadata){
            extractMetadataFromJPG((JpegImageMetadata) metadata);
        }
    }

    public void extractMetadataFromJPG(JpegImageMetadata imageMetadata){
        if(imageMetadata == null){
            return;
        }
        for (ImageMetadataItem rawMetadataItem : imageMetadata.getItems()) {
            String[]keyAndValue = parseExifRow(rawMetadataItem.toString());
            // TO DO : ADD LOGIC TO PUT DATA INTO metadataItems
        }
    }

    public String[] parseExifRow(String exifRow){
        if (exifRow == null || exifRow.isEmpty()) {
            return null;
        }
        exifRow = exifRow.trim();
        int indexOfSplit = exifRow.indexOf(':');
        String key = exifRow.substring(0, indexOfSplit).trim();
        String value = exifRow.substring(indexOfSplit+1).trim();
        return new String[]{key, value};
    }


}
