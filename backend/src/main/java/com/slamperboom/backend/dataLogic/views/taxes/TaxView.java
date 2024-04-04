package com.slamperboom.backend.dataLogic.views.taxes;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "DTO to view tax or factor data for some date ")
public record TaxView(Long id,
                      @Schema(description = "Name of tax/factor", example = "НДПИ") String taxName,
                      @Schema(description = "Type of data (TAX or FACTOR)") TaxType type,
                      @Schema(description = "Date of value", example = "2023-12-31") Date date,
                      Double value) {}
