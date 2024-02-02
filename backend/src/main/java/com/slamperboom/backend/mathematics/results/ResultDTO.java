package com.slamperboom.backend.mathematics.results;

import java.util.List;

public class ResultDTO {
    String taxName;
    String methodName;
    List<SeriesValueDTO> referenceValues;
    List<SeriesValueDTO> precisionValues;
    List<MathErrorDTO> mathErrors;

    public ResultDTO(String taxName, String methodName, List<SeriesValueDTO> referenceValues, List<SeriesValueDTO> precisionValues, List<MathErrorDTO> mathErrors) {
        this.taxName = taxName;
        this.methodName = methodName;
        this.referenceValues = referenceValues;
        this.precisionValues = precisionValues;
        this.mathErrors = mathErrors;
    }
}
