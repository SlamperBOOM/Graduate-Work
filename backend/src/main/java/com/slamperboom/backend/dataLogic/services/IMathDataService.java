package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.resultsDTO.MathErrorDTO;
import com.slamperboom.backend.mathematics.resultsDTO.PredictionResultDTO;

import java.util.List;

public interface IMathDataService {
    List<PredictionResultDTO> fetchResultsForTax(AlgorithmValues values, String taxName);
    List<List<MathErrorDTO>> getErrorsForTaxPredictions(String taxName);
    AlgorithmValues fetchValuesForAlgorithm(String taxName);
}
