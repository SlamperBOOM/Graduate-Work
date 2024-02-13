package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.mathematics.results.PredictionResultDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IControllerDataService {
    List<PredictionResultDTO> getResultsForTax(String taxName);
    void parseFileAndAddTaxValues(MultipartFile file, String taxName);
    void parseFileAndAddFactorValues(MultipartFile file, String taxName);
}
