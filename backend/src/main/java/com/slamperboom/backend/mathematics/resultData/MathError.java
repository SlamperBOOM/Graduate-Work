package com.slamperboom.backend.mathematics.resultData;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class MathError {
    private final String methodName;
    private final String errorName;
    private final double value;
    private Boolean isBetter;

    public void setBetter(boolean better) {
        isBetter = better;
    }
}
