package com.slamperboom.backend.mathematics.resultsDTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class MathErrorDTO {
    private final String methodName;
    private final String errorName;
    private final double value;
    private Boolean isBetter;

    public void setBetter(boolean better) {
        isBetter = better;
    }
}
