package com.slamperboom.backend.controllers;

import com.slamperboom.backend.dataLogic.services.IControllerDataService;
import com.slamperboom.backend.frontendDTO.*;
import com.slamperboom.backend.mathematics.ImplementedEntitiesService;
import com.slamperboom.backend.mathematics.MathService;
import com.slamperboom.backend.mathematics.resultData.PredictionResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Prediction API", description = "Make predictions and fetch related data")
@RestController
@RequestMapping("prediction")
@RequiredArgsConstructor
public class PredictionController {
    private final MathService mathService;
    private final ResultCodeManager resultCodeManager;
    private final IControllerDataService dataService;
    private final ImplementedEntitiesService implementedEntitiesService;

    @Operation(
            summary = "Make prediction with parameters",
            description = "Making prediction for tax using algorithm and its parameters. All fields must be specified in request body. " +
                    "You can get available algorithms with its parameters description (if any specified) with /prediction/algorithms. " +
                    "You must confirm or reject result got with this method by making request to /*link to confirm method*/"
    )
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "500",
            description = "Something happened while calculating prediction. Watch response for more accurate information",
            content = @Content(schema = @Schema()))
    @PostMapping("predict")
    public PredictionForFrontendDTO makePrecision(@RequestBody PredictionRequestDTO requestDTO){
        List<PredictionResultDTO> results = mathService.makePrediction(requestDTO.taxName(), requestDTO.methodName(), requestDTO.params())
                .stream().map(PredictionResult::mapToResultDTO).toList();
        return new PredictionForFrontendDTO(resultCodeManager.getNextUid(results.get(0)), results);
    }

    @Operation(
            summary = "Get all done predictions for tax",
            parameters = {
                    @Parameter(name = "taxname", description = "Tax name for which you need to get results", required = true)
            }
    )
    @GetMapping("predicts/get")
    public List<PredictionResultDTO> getPredictionsForTax(@RequestParam(name = "taxname") String taxName){
        return dataService.getResultsForTax(taxName).stream().map(PredictionResult::mapToResultDTO).toList();
    }

    @Operation(
            summary = "Get implemented algorithms",
            description = "Get implemented algorithms, its descriptions and all related parameters"
    )
    @GetMapping("algorithms")
    public List<AlgorithmDTO> getAlgorithms(){
        return implementedEntitiesService.getAlgorithmsDescription();
    }

    @Operation(
            summary = "Confirm prediction",
            description = "Confirmation or cancelling prediction result. " +
                    "Must provide result code that was given on predict request"
    )
    @PostMapping("confirm")
    public void uploadDecision(@RequestBody PredictionConfirmDTO predictionConfirmDTO){
        PredictionResultDTO resultDTO = resultCodeManager.checkUid(predictionConfirmDTO.resultCode());
        if(predictionConfirmDTO.confirm()) {
            dataService.savePredictionResult(PredictionResult.mapToPredictionResult(resultDTO));
        }
    }
}
