package com.gregorybroche.imageresolver.interfaces;

import com.gregorybroche.imageresolver.classes.ImageTemplate;

/**
 * functional interface to create callbacks and emit submitted data on add template form
 */
public interface TemplateAddFormSubmitListener {
    void onFormSubmit(ImageTemplate submittedTemplate);
}
