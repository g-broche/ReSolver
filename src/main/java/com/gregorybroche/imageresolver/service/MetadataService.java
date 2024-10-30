package com.gregorybroche.imageresolver.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.springframework.stereotype.Service;

import com.gregorybroche.imageresolver.Enums.MetadataFormat;
import com.gregorybroche.imageresolver.Enums.MetadataKey;
import com.gregorybroche.imageresolver.classes.MetadataItem;

@Service
public class MetadataService {
    private List<MetadataItem> metadataItems = new ArrayList<MetadataItem>();
    private Map<String, MetadataKey> exifKeysToMetadataKeys;

    public MetadataService(){
        this.exifKeysToMetadataKeys = MetadataKey.generateExifKeysToMetadataKeyMap();
    }

    public List<MetadataItem> getMetadataItems() {
        return metadataItems;
    }

    public void setMetadataItems(List<MetadataItem> metadataItems) {
        this.metadataItems = metadataItems;
    }

    public Map<String, MetadataKey> getExifKeysToMetadataKeys() {
        return exifKeysToMetadataKeys;
    }

    /**
     * gets metadata from an image file
     * @param file source file
     * @return instance of ImageMetadata
     * @throws ImagingException
     * @throws IOException
     */
    public ImageMetadata getMetadataFromFile(File file) throws ImagingException, IOException {
        ImageMetadata metadata = Imaging.getMetadata(file);
        return metadata;
    }

    /**
     * given a file, will print in output stream the metadata of the file
     * @param file
     * @throws ImagingException
     * @throws IOException
     */
    public void printMetadata(File file) throws ImagingException, IOException {
        ImageMetadata metadata = Imaging.getMetadata(file);
        System.out.println(metadata);
    }

    /**
     * parse the metadata of an image file and returns in the form of a list of MetadataItem instances
     * @param file
     * @return List of MetadataItems or null if format is not handled
     * @throws ImagingException
     * @throws IOException
     */
    public List<MetadataItem> extractMetadata(File file) throws ImagingException, IOException{
        ImageMetadata metadata = getMetadataFromFile(file);
        if (metadata instanceof JpegImageMetadata){
            return extractMetadataFromJPG((JpegImageMetadata) metadata);
        }
        return null;
    }

    /**
     * parsing and extraction method specific to JPG format
     * @param imageMetadata instance of JpegImageMetadata
     * @return List of MetadataItems or null if given imageMetadata is null
     */
    public List<MetadataItem> extractMetadataFromJPG(JpegImageMetadata imageMetadata){
        if(imageMetadata == null){
            return null;
        }
        List<MetadataItem> metadataItems = new ArrayList<MetadataItem>();
        for (ImageMetadataItem rawMetadataItem : imageMetadata.getItems()) {
            String[]keyAndValue = parseExifRow(rawMetadataItem.toString());
            if(isParsedMetadataRowValid(keyAndValue, MetadataFormat.EXIF)){
                MetadataItem newMetadataInfo = new MetadataItem(
                    MetadataKey.findMetadataKeyByExifKey(keyAndValue[0]),
                    keyAndValue[1]
                    );
                metadataItems.add(newMetadataInfo);
            }
        }
        return metadataItems;
    }

    /**
     * Method for parsing a row of raw exif metadata
     * @param exifRow
     * @return String[] [exifKey, exifValue] or null if parsing of input is invalid
     */
    public String[] parseExifRow(String exifRow){
        if (exifRow == null || exifRow.isEmpty()) {
            return null;
        }
        exifRow = exifRow.trim();
        int indexOfSplit = exifRow.indexOf(':');
        if (indexOfSplit == -1) {
            return null;
        }
        String key = exifRow.substring(0, indexOfSplit).trim();
        String value = exifRow.substring(indexOfSplit+1).trim();
        return new String[]{key, value};
    }

    /**
     * checks if a parsed Metadata row is a String[2] and the value of it's first index correspond to an expected key for its format
     * @param parsedRow
     * @param metadataFormat MetadataFormat enum to defined which metadata format key listing to check for parsedRow[0]
     * @return true if validated, false otherwise
     */
    public boolean isParsedMetadataRowValid(String[] parsedRow, MetadataFormat metadataFormat){
        if (parsedRow == null || parsedRow.length != 2){
            return false;
        }
        if (metadataFormat == MetadataFormat.EXIF && this.exifKeysToMetadataKeys.containsKey(parsedRow[0].toLowerCase())){
            return true;
        }
        return false;
    }
}
