package com.slamperboom.backend.frontendDTO;

import com.slamperboom.backend.mathematics.resultData.MathError;
import com.slamperboom.backend.mathematics.resultData.ResultParameter;
import com.slamperboom.backend.mathematics.resultData.SeriesValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Data for prediction")
public record PredictionResultDTO(@Schema(description = "Name of tax that prediction made for") String taxName,
                                  @Schema(description = "Algorithm with which prediction was made") String methodName,
                                  @Schema(description = "List of real tax values") List<SeriesValue> referenceValues,
                                  @Schema(description = "List of predicted tax values " +
                                          "with one extra predicted value for next period") List<SeriesValue> predictionValues,
                                  @Schema(description = "List of calculated errors") List<MathError> mathErrors,
                                  @Schema(description = "List of parameters for prediction") List<ResultParameter> parameters) {
}
