package com.slamperboom.backend.controllers;

import com.slamperboom.backend.frontendDTO.AlgorithmDTO;
import com.slamperboom.backend.frontendDTO.PredictionRequestDTO;
import com.slamperboom.backend.mathematics.MathService;
import com.slamperboom.backend.mathematics.results.ResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("prediction")
@RequiredArgsConstructor
public class PredictionController {
    private final MathService mathService;

    @GetMapping("predict")
    public List<ResultDTO> makePrecision(@RequestBody PredictionRequestDTO requestDTO){
        return mathService.makePrediction(requestDTO.taxName(), requestDTO.methodName(), requestDTO.params());
    }

    @GetMapping("get")
    public List<ResultDTO> getPredictionsForTax(@RequestParam(name = "taxname") String taxName){
        return new ArrayList<>();
    }

    @GetMapping("algorithms")
    public List<AlgorithmDTO> getAlgorithms(){
        return mathService.getMethods();
    }
}
