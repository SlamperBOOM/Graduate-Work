package com.slamperboom.backend.mathematics.resultData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Parameter of prediction")
public record ResultParameter(@Schema(description = "Name of parameter") String paramName,
                              Double value) {
}
