package com.slamperboom.backend.controllers;

import com.slamperboom.backend.dataLogic.services.IControllerDataService;
import com.slamperboom.backend.frontendDTO.AlgorithmDTO;
import com.slamperboom.backend.frontendDTO.PredictionConfirmDTO;
import com.slamperboom.backend.frontendDTO.PredictionForFrontendDTO;
import com.slamperboom.backend.frontendDTO.PredictionRequestDTO;
import com.slamperboom.backend.mathematics.ImplementedEntitiesService;
import com.slamperboom.backend.mathematics.MathService;
import com.slamperboom.backend.mathematics.resultData.PredictionResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Prediction controller", description = "Make predictions and fetch related data")
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
                    "You can get available algorithms with its parameters description (if any specified) with /*link to algorithms method*/. " +
                    "You must confirm or regret result got with this method by making request to /*link to confirm method*/"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500",
                    description = "Something happened while calculating prediction. Watch response for more accurate information",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping("predict")
    public PredictionForFrontendDTO makePrecision(@RequestBody PredictionRequestDTO requestDTO){
        return new PredictionForFrontendDTO(resultCodeManager.getNextUid(),
                mathService.makePrediction(requestDTO.taxName(), requestDTO.methodName(), requestDTO.params())
                        .stream().map(PredictionResult::mapToResultDTO).toList());
    }

    @Operation(
            summary = "Get already done predictions for tax",
            description = "Get all done predictions for tax",
            parameters = {
                    @Parameter(name = "taxname", description = "Tax name for which you need to get results", required = true)
            }
    )
    @GetMapping("predicts/get")
    public List<PredictionResult> getPredictionsForTax(@RequestParam(name = "taxname") String taxName){
        return dataService.getResultsForTax(taxName);
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
            description = ""
    )
    @PostMapping("confirm")
    public void uploadDecision(@RequestBody PredictionConfirmDTO predictionConfirmDTO){
        if(resultCodeManager.checkUid(predictionConfirmDTO.resultCode()) &&
            predictionConfirmDTO.resultDTO() != null) {
            dataService.savePredictionResult(PredictionResult.mapToPredictionResult(predictionConfirmDTO.resultDTO()));
        }
    }
}
