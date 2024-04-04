package com.slamperboom.backend.mathematics.resultData;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
@Schema(description = "DTO for math error (aka metric)")
public class MathError {
    @Schema(description = "Algorithm name that linked with this error", example = "ARIMA")
    private final String methodName;

    @Schema(description = "Name of math error", example = "MRSE")
    private final String errorName;

    private final double value;

    @Schema(description = "Is this math error better than other among same kind of errors for other algorithms")
    private Boolean isBetter;

    public void setBetter(boolean better) {
        isBetter = better;
    }
}
