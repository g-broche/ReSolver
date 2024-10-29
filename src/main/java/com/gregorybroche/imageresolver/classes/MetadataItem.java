package com.gregorybroche.imageresolver.classes;

import com.gregorybroche.imageresolver.Enums.MetadataKey;

public class MetadataItem {
    private MetadataKey metadataKey;
    private String value;

    public MetadataItem(MetadataKey metadataKey, String value){
        this.metadataKey = metadataKey;
        this.value = value;
    }

    public MetadataKey getmetadataKey() {
        return metadataKey;
    }
    
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
