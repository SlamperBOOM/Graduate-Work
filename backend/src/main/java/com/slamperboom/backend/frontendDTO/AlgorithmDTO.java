package com.slamperboom.backend.frontendDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Algorithm description and list of linked parameters")
public record AlgorithmDTO(@Schema(description = "Name of the algorithm", example = "ARIMA") String methodName,
                           @Schema(description = "Description of the algorithm") String methodDescription,
                           @Schema(description = "List of parameters with theirs descriptions") List<String> parameters) {
}
