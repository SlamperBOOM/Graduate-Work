package com.slamperboom.backend.frontendDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO to view tax-factor link info")
public record TaxFactorDTO(Long id,
                           @Schema(description = "Tax to create link with", example = "НДПИ") String taxName,
                           @Schema(description = "Factor to link", example = "Добыча нефти") String factorName) {
}
