package com.slamperboom.backend.mathematics.resultsDTO;

import com.slamperboom.backend.frontendDTO.ResultDTO;
import lombok.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class PredictionResultDTO {
    private final String taxName;
    private final String methodName;
    private final List<SeriesValueDTO> referenceValues;
    private final List<SeriesValueDTO> predictionValues;
    @Setter
    private List<MathErrorDTO> mathErrors = new LinkedList<>();
    private final List<ResultParameterDTO> parameters;

    public static PredictionResultDTO createInstanceFromRawWithoutErrors(String taxName,
                               String methodName,
                               List<Date> dates,
                               List<Double> referenceValues,
                               List<Double> predictionValues,
                               List<ResultParameterDTO> parameters) {
        return new PredictionResultDTO(
                taxName,
                methodName,
                convertSeries(dates, referenceValues),
                convertSeries(dates, predictionValues),
                parameters);
    }

    public static PredictionResultDTO createInstanceFromRawWithErrors(String taxName,
                               String methodName,
                               List<Date> dates,
                               List<Double> referenceValues,
                               List<Double> predictionValues,
                               List<MathErrorDTO> mathErrors,
                               List<ResultParameterDTO> parameters) {
        return new PredictionResultDTO(
                taxName,
                methodName,
                convertSeries(dates, referenceValues),
                convertSeries(dates, predictionValues),
                mathErrors,
                parameters);
    }

    public static ResultDTO mapToResultDTO(PredictionResultDTO resultDTO){
        return new ResultDTO(resultDTO.getTaxName(),
                resultDTO.getMethodName(),
                resultDTO.getReferenceValues(),
                resultDTO.getPredictionValues(),
                resultDTO.getMathErrors(),
                resultDTO.getParameters());
    }

    public static PredictionResultDTO mapToPredictionResult(ResultDTO resultDTO){
        return new PredictionResultDTO(resultDTO.taxName(),
                resultDTO.methodName(),
                resultDTO.referenceValues(),
                resultDTO.predictionValues(),
                resultDTO.mathErrors(),
                resultDTO.parameters());
    }

    private static List<SeriesValueDTO> convertSeries(List<Date> dates,
                                               List<Double> seriesValues){
        List<SeriesValueDTO> series = new LinkedList<>();
        for(int i=0; i<seriesValues.size(); ++i){
            series.add(new SeriesValueDTO(dates.get(i), seriesValues.get(i)));
        }
        return series;
    }
}
