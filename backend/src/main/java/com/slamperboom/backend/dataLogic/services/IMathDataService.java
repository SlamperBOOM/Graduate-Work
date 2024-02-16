package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.results.MathErrorDTO;
import com.slamperboom.backend.mathematics.results.PredictionResultDTO;
import com.slamperboom.backend.mathematics.results.ResultParameterDTO;

import java.util.Date;
import java.util.List;

public interface IMathDataService {
    List<PredictionResultDTO> fetchResultsForTax(AlgorithmValues values, String taxName);
    List<List<MathErrorDTO>> getErrorsForTaxPredictions(String taxName);
    AlgorithmValues fetchValuesForAlgorithm(String taxName);
}
