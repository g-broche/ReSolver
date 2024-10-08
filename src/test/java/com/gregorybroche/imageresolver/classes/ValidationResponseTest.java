package com.gregorybroche.imageresolver.classes;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ValidationResponseTest {


    @Test
        void constructor_passingNullParameters_ShouldConstructAndIsNullPropertyGettersReturnTrue() {
        ValidationResponse validationResponse = new ValidationResponse(false, null, null);
        assertFalse(validationResponse.getIsSuccess());
        assertTrue(validationResponse.isDataNull());
        assertTrue(validationResponse.isMessageNull());
    }
}
