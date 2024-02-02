package com.slamperboom.backend.mathematics.algorithms;

import java.util.List;

public interface PrecisionAlgorithm {
    /**
     *
     * @param referenceValues содержит реальные значения налога и, опционально, значения параметров, влияющих на налог
     * @param parameters необходимые для метода параметры
     * @return набор спрогнозированных значений + прогнозное значение на следующий календарный период
     */
    List<Double> makePrecision(AlgorithmValues referenceValues, AlgorithmParameters parameters);
    String getName();
    AlgorithmParameters getParameters();
    String getDescription();
}
