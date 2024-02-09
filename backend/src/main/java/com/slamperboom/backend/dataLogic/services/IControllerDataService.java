package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.mathematics.results.ResultDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IControllerDataService {
    List<ResultDTO> getResultsForTax(String taxName);
    void parseFileAndAddTaxValues(MultipartFile file, String taxName);
    void parseFileAndAddFactorValues(MultipartFile file, String taxName);
}
