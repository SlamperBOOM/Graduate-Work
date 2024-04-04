package com.slamperboom.backend.frontendDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO for create prediction request")
public record PredictionRequestDTO(@Schema(description = "Tax to make prediction for", example = "НДПИ") String taxName,
                                   @Schema(description = "Algorithm with which to make prediction with", example = "ARIMA") String methodName,
                                   @Schema(description = "List of parameters values as described in description for algorithm") List<String> params) {
}
