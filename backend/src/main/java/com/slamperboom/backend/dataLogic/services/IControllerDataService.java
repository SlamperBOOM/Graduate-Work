package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.mathematics.resultData.PredictionResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IControllerDataService {
    List<PredictionResult> getResultsForTax(String taxName);
    void savePredictionResult(PredictionResult resultDTO);
    void parseFileAndAddTaxValues(MultipartFile file, String taxName);
    void parseFileAndAddFactorValues(MultipartFile file, String taxName);
}
