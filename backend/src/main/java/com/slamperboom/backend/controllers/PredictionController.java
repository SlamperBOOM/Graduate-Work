package com.slamperboom.backend.controllers;

import com.slamperboom.backend.dataLogic.services.IControllerDataService;
import com.slamperboom.backend.frontendDTO.AlgorithmDTO;
import com.slamperboom.backend.frontendDTO.PredictionRequestDTO;
import com.slamperboom.backend.mathematics.ImplementedEntitiesService;
import com.slamperboom.backend.mathematics.MathService;
import com.slamperboom.backend.mathematics.results.PredictionResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("prediction")
@RequiredArgsConstructor
public class PredictionController {
    private final MathService mathService;
    private final IControllerDataService dataService;
    private final ImplementedEntitiesService implementedEntitiesService;

    @PostMapping("predict")
    public List<PredictionResultDTO> makePrecision(@RequestBody PredictionRequestDTO requestDTO){
        return mathService.makePrediction(requestDTO.taxName(), requestDTO.methodName(), requestDTO.params());
    }

    @GetMapping("predicts/get")
    public List<PredictionResultDTO> getPredictionsForTax(@RequestParam(name = "taxname") String taxName){
        return dataService.getResultsForTax(taxName);
    }

    @GetMapping("algorithms")
    public List<AlgorithmDTO> getAlgorithms(){
        return implementedEntitiesService.getAlgorithmsDescription();
    }
}
