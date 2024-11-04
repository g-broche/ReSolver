package com.gregorybroche.imageresolver.Enums;

public enum ExifNode {
    ROOT(
        new MetadataKey[]{
            MetadataKey.MAKE,
            MetadataKey.MODEL,
            MetadataKey.ORIENTATION,
            MetadataKey.XRESOLUTION,
            MetadataKey.YRESOLUTION,
            MetadataKey.RESOLUTION_UNIT,
            MetadataKey.DATETIME,
            MetadataKey.AUTHOR,
            MetadataKey.COPYRIGHT,
        }
    ),
    EXIF(
        new MetadataKey[]{
            MetadataKey.FNUMBER,
            MetadataKey.FOCAL_LENGTH,
            MetadataKey.EXPOSURE_TIME,
            MetadataKey.WIDTH,
            MetadataKey.HEIGHT
        }
    );

    private final MetadataKey[] allowedMetadataKeys;

    ExifNode(MetadataKey[] allowedMetadataKeys){
        this.allowedMetadataKeys = allowedMetadataKeys;
    }

    public MetadataKey[] getKeys() {
        return allowedMetadataKeys;
    }
}
