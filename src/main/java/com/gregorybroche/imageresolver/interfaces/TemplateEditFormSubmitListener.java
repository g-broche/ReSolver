package com.gregorybroche.imageresolver.interfaces;

import com.gregorybroche.imageresolver.classes.ImageTemplate;

/**
 * functional interface to create callbacks and emit submitted data on edit template form
 */
public interface TemplateEditFormSubmitListener{
    void onFormSubmit(ImageTemplate submittedTemplate, int indexOfTemplateToEdit);
}
