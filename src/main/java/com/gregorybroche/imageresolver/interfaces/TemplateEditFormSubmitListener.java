package com.gregorybroche.imageresolver.interfaces;

import com.gregorybroche.imageresolver.classes.ImageTemplate;

public interface TemplateEditFormSubmitListener{
    void onFormSubmit(ImageTemplate submittedTemplate, int indexOfTemplateToEdit);
}
