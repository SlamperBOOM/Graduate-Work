package com.slamperboom.backend.mathematics.algorithms;

import lombok.Getter;

import java.util.Date;
import java.util.List;

/**
 * Хранит значения налога, значения факторов и соответствующие даты.
 * <p>Формат значений налога: [val1, val2, ...]
 * <p>Формат значений факторов:
 * <p>[
 * <p>[factor1_val1, factor1_val2, ...],
 * <p>[factor2_val1, factor2_val2, ...],
 * <p>...
 * <p>]
 * <p>При использовании экземпляра, созданного в {@code MathService}, гарантируется,
 * что и значения налога и значения факторов лежат на одних и тех же датах
 */
@Getter
public class AlgorithmValues {
    private List<Date> dates;
    private final List<Double> reference;
    private List<List<Double>> factors;

    public AlgorithmValues(List<Double> reference) {
        this.reference = reference;
    }

    public AlgorithmValues(List<Double> reference, List<List<Double>> factors){
        this.reference = reference;
        this.factors = factors;
    }

    public AlgorithmValues(List<Date> dates, List<Double> reference, List<List<Double>> factors) {
        this.dates = dates;
        this.reference = reference;
        this.factors = factors;
    }
}
