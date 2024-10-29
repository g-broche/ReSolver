package com.gregorybroche.imageresolver.Enums;

import java.util.HashMap;
import java.util.Map;

public enum MetadataKey {
    MAKE("make"),
    MODEL("Model"),
    ORIENTATION("Orientation"),
    XRESOLUTION("XResolution"),
    YRESOLUTION("YResolution"),
    RESOLUTION_UNIT("ResolutionUnit"),
    DATETIME("DateTime"),
    AUTHOR("Artist"),
    COPYRIGHT("Copyright"),
    FNUMBER("FNumber"),
    FOCAL_LENGTH("FocalLength"),
    EXPOSURE_TIME("ExposureTime"),
    WIDTH("ExifImageWidth"),
    HEIGHT("ExifImageLength");

    private final String exifKey;

    MetadataKey(String exifKey){
        this.exifKey = exifKey;
    }

    public String getExifKey() {
        return exifKey;
    }

    public static MetadataKey findMetadataKeyByExifKey(String exifKey){
        for (MetadataKey metadataKey : values()) {
            if (metadataKey.getExifKey().equalsIgnoreCase(exifKey)) {
                return metadataKey;
            }
        }
        return null;
    }

    public static Map<String, MetadataKey> getExifKeysToMetadataKey(){
        Map<String, MetadataKey> exifKeysToMetadataKeys = new HashMap<String, MetadataKey>();
        for (MetadataKey metadataKey : values()) {
            String exifKey = metadataKey.getExifKey();
            if (exifKey != null) {
                exifKeysToMetadataKeys.put(exifKey, metadataKey);
            }
        }
        return exifKeysToMetadataKeys;
    }
}
