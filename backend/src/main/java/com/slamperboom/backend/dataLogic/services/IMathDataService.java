package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.resultData.MathError;
import com.slamperboom.backend.mathematics.resultData.PredictionResult;

import java.util.List;

public interface IMathDataService {
    List<PredictionResult> fetchResultsForTax(AlgorithmValues values, String taxName);
    List<List<MathError>> getErrorsForTaxPredictions(String taxName);
    AlgorithmValues fetchValuesForAlgorithm(String taxName);
}
