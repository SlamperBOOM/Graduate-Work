package com.slamperboom.backend.dataLogic.views.taxes;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for creating tax-factor link")
public record TaxFactorCreateView(@Schema(description = "Tax to create link with", example = "НДПИ") String taxName,
                                  @Schema(description = "Factor to link", example = "Добыча нефти") String factorName) {}
