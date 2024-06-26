package com.slamperboom.backend.frontendDTO;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO to view tax or factor data for some date ")
public record TaxDTO(Long id,
                     @Schema(description = "Name of tax/factor", example = "НДПИ") String taxName,
                     @Schema(description = "Type of data (TAX or FACTOR)") TaxType type,
                     @Schema(description = "Date of value", example = "31-12-2023") String date,
                     Double value) {}
