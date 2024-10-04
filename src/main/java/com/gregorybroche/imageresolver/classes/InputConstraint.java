package com.gregorybroche.imageresolver.classes;

import com.gregorybroche.imageresolver.Enums.ConstraintType;

/**
 * Class intended to by used to define constraints for user submitted inputs
 */

public class InputConstraint {
    private String constraintName;
    private ConstraintType constraintType;
    private Object value;
    private String errorMessage;

    public InputConstraint(String constraintName, ConstraintType ConstraintType, Object value, String errorMessage) {
        this.constraintName = constraintName;
        this.constraintType = ConstraintType;
        this.value = value;
        this.errorMessage = errorMessage;
    }

    public String getConstraintName() {
        return this.constraintName;
    }

    public ConstraintType getConstraintType() {
        return this.constraintType;
    }

    public Object getValue() {
        return this.value;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
