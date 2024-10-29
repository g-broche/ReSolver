package com.gregorybroche.imageresolver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.gregorybroche.imageresolver.Enums.MetadataKey;

public class MetadataServiceTest {
    private MetadataService metadataService;

    public MetadataServiceTest(){
        this.metadataService = new MetadataService();
    }


    @Test
    void onConstruct_shouldHaveImportedExifToMetadataMap(){
        String testValue = MetadataKey.MAKE.getExifKey();
        assertEquals(metadataService.getExifKeysToMetadataKeys().get(testValue), MetadataKey.MAKE);
    }

    @Test
    void parseExifRow_onValidRowFormatInput_ShouldReturnArrayWithExpectedKeyAndValue(){
        String testRow = "DateTime: '2017:10:26 13:33:47'";
        String[] result = metadataService.parseExifRow(testRow);
        assertEquals(result[0], "DateTime");
        assertEquals(result[1], "'2017:10:26 13:33:47'");
    }
}
