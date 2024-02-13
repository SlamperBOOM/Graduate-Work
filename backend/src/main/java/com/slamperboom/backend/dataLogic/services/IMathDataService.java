package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.results.MathErrorDTO;
import com.slamperboom.backend.mathematics.results.PredictionResultDTO;
import com.slamperboom.backend.mathematics.results.ResultParameterDTO;

import java.util.Date;
import java.util.List;

public interface IMathDataService {
    void savePredictionResult(String taxName,
                              String methodName,
                              List<Date> dates,
                              List<Double> prediction,
                              List<MathErrorDTO> predictionErrors,
                              List<ResultParameterDTO> parameters);
    List<PredictionResultDTO> fetchResultsForTax(AlgorithmValues values, String taxName);
    List<List<MathErrorDTO>> getErrorsForTaxPredictions(String taxName);
    AlgorithmValues fetchValuesForAlgorithm(String taxName);
}
