package com.slamperboom.backend.mathematics.resultData;

import com.slamperboom.backend.frontendDTO.PredictionResultDTO;
import com.slamperboom.backend.utils.DateUtils;
import lombok.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class PredictionResult {
    private final String taxName;
    private final String methodName;
    private final List<SeriesValue> referenceValues;
    private final List<SeriesValue> predictionValues;
    @Setter
    private List<MathError> mathErrors = new LinkedList<>();
    private final List<ResultParameter> parameters;

    public static PredictionResult createInstanceFromRawWithoutErrors(String taxName,
                                                                      String methodName,
                                                                      List<Date> dates,
                                                                      List<Double> referenceValues,
                                                                      List<Double> predictionValues,
                                                                      List<ResultParameter> parameters) {
        return new PredictionResult(
                taxName,
                methodName,
                convertSeries(dates, referenceValues),
                convertSeries(dates, predictionValues),
                parameters);
    }

    public static PredictionResult createInstanceFromRawWithErrors(String taxName,
                                                                   String methodName,
                                                                   List<Date> dates,
                                                                   List<Double> referenceValues,
                                                                   List<Double> predictionValues,
                                                                   List<MathError> mathErrors,
                                                                   List<ResultParameter> parameters) {
        return new PredictionResult(
                taxName,
                methodName,
                convertSeries(dates, referenceValues),
                convertSeries(dates, predictionValues),
                mathErrors,
                parameters);
    }

    public static PredictionResultDTO mapToResultDTO(PredictionResult resultDTO){
        return new PredictionResultDTO(resultDTO.getTaxName(),
                resultDTO.getMethodName(),
                resultDTO.getReferenceValues(),
                resultDTO.getPredictionValues(),
                resultDTO.getMathErrors(),
                resultDTO.getParameters());
    }

    public static PredictionResult mapToPredictionResult(PredictionResultDTO predictionResultDTO){
        return new PredictionResult(predictionResultDTO.taxName(),
                predictionResultDTO.methodName(),
                predictionResultDTO.referenceValues(),
                predictionResultDTO.predictionValues(),
                predictionResultDTO.mathErrors(),
                predictionResultDTO.parameters());
    }

    private static List<SeriesValue> convertSeries(List<Date> dates,
                                                   List<Double> seriesValues){
        List<SeriesValue> series = new LinkedList<>();
        for(int i=0; i<seriesValues.size(); ++i){
            series.add(new SeriesValue(DateUtils.formatDateToString(dates.get(i)), seriesValues.get(i)));
        }
        return series;
    }
}
