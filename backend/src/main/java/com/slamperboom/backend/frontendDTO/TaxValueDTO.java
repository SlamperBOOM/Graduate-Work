package com.slamperboom.backend.frontendDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "DTO to describe value for some tax or factor")
public record TaxValueDTO(@Schema(description = "Name of tax/factor", example = "НДПИ") String taxName,
                          @Schema(description = "Date of value", example = "31-12-2023") String date,
                          Double value) {
}
