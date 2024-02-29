package com.slamperboom.backend.frontendDTO;

import com.slamperboom.backend.mathematics.resultData.MathError;
import com.slamperboom.backend.mathematics.resultData.ResultParameter;
import com.slamperboom.backend.mathematics.resultData.SeriesValue;

import java.util.List;

public record ResultDTO(String taxName, String methodName, List<SeriesValue> referenceValues,
                        List<SeriesValue> predictionValues, List<MathError> mathErrors, List<ResultParameter> parameters) {
}
