package com.gregorybroche.imageresolver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.gregorybroche.imageresolver.Enums.MetadataFormat;
import com.gregorybroche.imageresolver.Enums.MetadataKey;
import com.gregorybroche.imageresolver.classes.MetadataItem;

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

    @Test
    void isParsedMetadataRowValid_parsedRowIsNull_ShouldReturnFalse(){
        String[] parsedRow = null;
        assertFalse(metadataService.isParsedMetadataRowValid(parsedRow, MetadataFormat.EXIF));
    }

    @Test
    void isParsedMetadataRowValid_parsedRowHasLessThan2Entries_ShouldReturnFalse(){
        String[] parsedRow = new String[]{"DateTime"};
        assertFalse(metadataService.isParsedMetadataRowValid(parsedRow, MetadataFormat.EXIF));
    }

    @Test
    void isParsedMetadataRowValid_parsedRowHasMoreThan2Entries_ShouldReturnFalse(){
        String[] parsedRow = new String[]{"DateTime", "'2017:10:26 13:33:47'", "extra entry"};
        assertFalse(metadataService.isParsedMetadataRowValid(parsedRow, MetadataFormat.EXIF));
    }

    @Test
    void isParsedMetadataRowValid_parsedRowHas2EntriesButKeyIsNotValidExifKey_ShouldReturnFalse(){
        String[] parsedRow = new String[]{"invalidKey", "'2017:10:26 13:33:47'"};
        assertFalse(metadataService.isParsedMetadataRowValid(parsedRow, MetadataFormat.EXIF));
    }

    @Test
    void isParsedMetadataRowValid_parsedRowHas2EntriesAndKeyIsValidExifKey_ShouldReturnTrue(){
        String[] parsedRow = new String[]{"DateTime", "'2017:10:26 13:33:47'"};
        assertTrue(metadataService.isParsedMetadataRowValid(parsedRow, MetadataFormat.EXIF));
    }

    @Test
    void setMetadataItems_parsingAndValidatingCorrectDataThenSetting_ShouldSetDataAsIntended(){
        String dateTimeRow = "DateTime: '2017:10:26 13:33:47'";
        String[] parsedDateTimeRow = metadataService.parseExifRow(dateTimeRow);
        String makeRow = "Make: 'Nikkon'";
        String[] parsedMakeRow = metadataService.parseExifRow(makeRow);
        String widthRow = "ExifImageWidth: 4000";
        String[] parsedWidthRow = metadataService.parseExifRow(widthRow);
        List<MetadataItem> extractedMetadataItems = new ArrayList<MetadataItem>();
        if(
            metadataService.isParsedMetadataRowValid(parsedDateTimeRow, MetadataFormat.EXIF)
            && metadataService.isParsedMetadataRowValid(parsedMakeRow, MetadataFormat.EXIF)
            && metadataService.isParsedMetadataRowValid(parsedWidthRow, MetadataFormat.EXIF)
        ){
            extractedMetadataItems.add(new MetadataItem(MetadataKey.findMetadataKeyByExifKey(parsedDateTimeRow[0]), parsedDateTimeRow[1]));
            extractedMetadataItems.add(new MetadataItem(MetadataKey.findMetadataKeyByExifKey(parsedMakeRow[0]), parsedMakeRow[1]));
            extractedMetadataItems.add(new MetadataItem(MetadataKey.findMetadataKeyByExifKey(parsedWidthRow[0]), parsedWidthRow[1]));
        }
        metadataService.setMetadataItems(extractedMetadataItems);
        List<MetadataItem> setItems = metadataService.getMetadataItems();
        MetadataItem firstEntry = setItems.get(0);
        MetadataItem secondEntry = setItems.get(1);
        MetadataItem thirdEntry = setItems.get(2);
        assertEquals(firstEntry.getmetadataKey(), MetadataKey.DATETIME);
        assertEquals(firstEntry.getValue(), "'2017:10:26 13:33:47'");
        assertEquals(secondEntry.getmetadataKey(), MetadataKey.MAKE);
        assertEquals(secondEntry.getValue(), "'Nikkon'");
        assertEquals(thirdEntry.getmetadataKey(), MetadataKey.WIDTH);
        assertEquals(thirdEntry.getValue(), "4000");
    }
}
