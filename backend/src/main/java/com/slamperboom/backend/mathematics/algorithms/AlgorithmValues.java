package com.slamperboom.backend.mathematics.algorithms;

import java.util.Date;
import java.util.List;

public class AlgorithmValues {
    private final List<Date> dates;
    private final List<Double> reference;
    private List<List<Double>> factors;

    public AlgorithmValues(List<Date> dates, List<Double> reference) {
        this.dates = dates;
        this.reference = reference;
    }

    public void setFactors(List<List<Double>> factors) {
        this.factors = factors;
    }

    public List<Date> getDates() {
        return dates;
    }

    public List<Double> getReference() {
        return reference;
    }

    public List<List<Double>> getFactors() {
        return factors;
    }
}
