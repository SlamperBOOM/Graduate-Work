package com.slamperboom.backend.mathematics.algorithms;

import com.slamperboom.backend.mathematics.resultData.ResultParameter;

import java.util.Collections;
import java.util.List;

public interface PredictionAlgorithm {
    /**
     *
     * @param referenceValues содержит реальные значения налога и, опционально, значения параметров, влияющих на налог
     * @param parameters необходимые для метода параметры
     * @return набор спрогнозированных значений + прогнозное значение на следующий календарный период
     */
    List<Double> makePrediction(AlgorithmValues referenceValues, AlgorithmParameters parameters);
    String getName();
    AlgorithmParameters getParameters();
    default String getDescription(){
        return "";
    }
    /**
     * Возвращает список параметров, при которых был найден прогноз в последнем вызове makePrediction
     * @return Список параметров
     */
    default List<ResultParameter> getPredictionParameters(){
        return Collections.emptyList();
    }
}
