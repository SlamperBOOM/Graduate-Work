package com.slamperboom.backend.frontendDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO to return prediction result")
public record PredictionForFrontendDTO(@Schema(description = "Code that you should use when confirming or rejecting result",
                                        example = "9833865b-cf43-44b1-99ff-9a1258bdef72") String resultCode,
                                       @Schema(description = "List of results. First value is always result of prediction, " +
                                               "others are predictions with other algorithms for same tax") List<PredictionResultDTO> results) {
}