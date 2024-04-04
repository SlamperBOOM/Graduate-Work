package com.slamperboom.backend.frontendDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO to confirm or reject calculated prediction")
public record PredictionConfirmDTO(@Schema(description = "Code of prediction",
                                    example = "9833865b-cf43-44b1-99ff-9a1258bdef72") String resultCode,
                                   @Schema(description = "true if you want to save prediction with that code, " +
                                           "or false if you want to reject it") boolean confirm) {
}
