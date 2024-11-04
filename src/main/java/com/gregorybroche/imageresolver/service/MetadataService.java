package com.gregorybroche.imageresolver.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import com.gregorybroche.imageresolver.Enums.ExifNode;
import com.gregorybroche.imageresolver.Enums.MetadataFormat;
import com.gregorybroche.imageresolver.Enums.MetadataKey;
import com.gregorybroche.imageresolver.classes.MetadataItem;
import com.gregorybroche.imageresolver.classes.ValidationResponse;

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

    public boolean isMetadataLoaded(){
        return this.metadataItems != null && this.metadataItems.size()>0;
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
     * for ease of use in dev prints in output stream the metadata list loaded by the service
     */
    public void printLoadedMetadata() {
        if(this.metadataItems.isEmpty()){
            System.out.println("Metadata service doesn't have any loaded metadata");
            return;
        }
        System.out.println("loaded metadata : ");
        for (MetadataItem metadataItem : this.metadataItems) {
            System.out.println(metadataItem.getmetadataKey().toString()+" : "+metadataItem.getValue());
        }
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

    /**
     * Method to write loaded metadata info into a jpeg compatible file. Does not currently work properly to import data
     * except for JPEGvariety nodes
     * @param file
     * @return
     */
    public ValidationResponse writeLoadedMetadataToJPEGFile(File file){
        ImageOutputStream outputStream = null;
        ImageWriter writer = null;
        try {
            writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            IIOMetadata metadata = writer.getDefaultImageMetadata(ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB), null);
            IIOMetadataNode root = createJPEGRoot();
            IIOMetadataNode markerSequence = new IIOMetadataNode("markerSequence");
            root.appendChild(markerSequence);
            IIOMetadataNode exifNode = createEXIFMetadataStructure();
            markerSequence.appendChild(exifNode);

            FillExifNode(exifNode, this.metadataItems);

            metadata.mergeTree("javax_imageio_jpeg_image_1.0", root);

            BufferedImage image = ImageIO.read(file);
            outputStream = ImageIO.createImageOutputStream(file);
            writer.setOutput(outputStream);
            writer.write(new IIOImage(image, null, metadata));
            return new ValidationResponse(true, null, null);
        } catch (Exception e) {
            System.err.println("error metadata : "+e.getMessage());
            return new ValidationResponse(false, null, "could not write metadata to file, error : "+e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                writer.dispose();
            }
        }

    }

    private IIOMetadataNode createJPEGRoot(){
        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_jpeg_image_1.0");

        IIOMetadataNode jpegVariety = new IIOMetadataNode("JPEGvariety");
        IIOMetadataNode app0JFIF = new IIOMetadataNode("app0JFIF");
        app0JFIF.setAttribute("majorVersion", "1");
        app0JFIF.setAttribute("minorVersion", "2");
        app0JFIF.setAttribute("resUnits", "1");
        app0JFIF.setAttribute("Xdensity", "90");
        app0JFIF.setAttribute("Ydensity", "90");
        app0JFIF.setAttribute("thumbWidth", "0");
        app0JFIF.setAttribute("thumbHeight", "0");
        jpegVariety.appendChild(app0JFIF);
        root.appendChild(jpegVariety);

        return root;
    }

    private IIOMetadataNode createEXIFMetadataStructure(){
        IIOMetadataNode exifNode = new IIOMetadataNode("unknown");
        exifNode.setAttribute("MarkerTag", "225");  // Marker tag for APP1/Exif
        return exifNode;
    }

    private void FillExifNode(IIOMetadataNode exifNode, List<MetadataItem> metadataItems){
        List<MetadataKey> allowedKeysForRoot = Arrays.asList(ExifNode.ROOT.getKeys());
        for (MetadataItem metadataItem : metadataItems) {
            if (allowedKeysForRoot.contains(metadataItem.getmetadataKey())) {
                addMetadataItemToExifMetadataNode(exifNode, metadataItem);
            }
        }
    }

    private void addMetadataItemToExifMetadataNode(IIOMetadataNode metadataNodeToAppendTo, MetadataItem metadataItem){
        String exifAttribute = metadataItem.getmetadataKey().getExifKey();
        String exifValue = metadataItem.getValue();
        IIOMetadataNode newNode = new IIOMetadataNode(exifAttribute);
        newNode.setAttribute("value", exifValue);
        metadataNodeToAppendTo.appendChild(newNode);
        if (metadataItem.getmetadataKey() == MetadataKey.MODEL) {
            System.out.println("Metadata Model : "+exifAttribute+" -> "+exifValue);
        }
    }

    // METHODS FOR REVERSE ENGINEERING OF JPEG NO STRUCTURE

    public void readSourceMetadataNodes(File file){
        try {
            ImageReader reader = ImageIO.getImageReadersByFormatName("jpeg").next();
            reader.setInput(ImageIO.createImageInputStream(file));
            IIOMetadata metadata = reader.getImageMetadata(0);
            String[] metadataFormatNames = metadata.getMetadataFormatNames();
    
            for (String formatName : metadataFormatNames) {
                System.out.println("Format: " + formatName);
                printMetadata(metadata.getAsTree(formatName), 0);
            }
        } catch (Exception e) {
            System.err.println("couldn't print metadata nodes, error : "+e.getMessage());
        }

    }

    private static void printMetadata(Node node, int indent) {
        for (int i = 0; i < indent; i++) System.out.print("  ");
        System.out.println(node.getNodeName() + " " + node.getAttributes());
        Node child = node.getFirstChild();
        System.out.println(node.getNodeName());
        while (child != null) {
            if (!child.hasChildNodes()) {
                System.out.println("node name : '"+child.getNodeName()+"' ; node value : '"+child.getNodeValue()+"'");
            }
            printMetadata(child, indent + 1);

            child = child.getNextSibling();
        }
    }
}
