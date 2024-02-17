package com.slamperboom.backend.frontendDTO;

import com.slamperboom.backend.mathematics.resultsDTO.MathErrorDTO;
import com.slamperboom.backend.mathematics.resultsDTO.ResultParameterDTO;
import com.slamperboom.backend.mathematics.resultsDTO.SeriesValueDTO;

import java.util.List;

public record ResultDTO(String taxName, String methodName, List<SeriesValueDTO> referenceValues,
                        List<SeriesValueDTO> predictionValues, List<MathErrorDTO> mathErrors, List<ResultParameterDTO> parameters) {
}
