package com.slamperboom.backend.frontendDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for creating tax-factor link")
public record TaxFactorCreateDTO(@Schema(description = "Tax to create link with", example = "НДПИ") String taxName,
                                 @Schema(description = "Factor to link", example = "Добыча нефти") String factorName) {}
