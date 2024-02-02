package com.slamperboom.backend.mathematics.results;

public class MathErrorDTO {
    String errorName;
    double value;
    Boolean isBetter;

    public MathErrorDTO(String errorName, double value) {
        this.errorName = errorName;
        this.value = value;
    }

    public void setBetter(boolean better) {
        isBetter = better;
    }

    public String getErrorName() {
        return errorName;
    }

    public double getValue() {
        return value;
    }

    public boolean isBetter() {
        return isBetter;
    }
}
