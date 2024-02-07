package com.slamperboom.backend.mathematics.results;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@ToString
public class ResultDTO {
    private final String taxName;
    private final String methodName;
    private List<SeriesValueDTO> referenceValues;
    private List<SeriesValueDTO> predictionValues;
    @Setter
    private List<MathErrorDTO> mathErrors;
    private final List<ResultParameterDTO> parameters;

    public ResultDTO(String taxName,
                     String methodName,
                     List<Date> dates,
                     List<Double> referenceValues,
                     List<Double> predictionValues,
                     List<ResultParameterDTO> parameters) {
        this.taxName = taxName;
        this.methodName = methodName;
        convertReferenceAndPrediction(dates, referenceValues, predictionValues);
        this.parameters = parameters;
    }

    public ResultDTO(String taxName,
                     String methodName,
                     List<Date> dates,
                     List<Double> referenceValues,
                     List<Double> predictionValues,
                     List<MathErrorDTO> mathErrors,
                     List<ResultParameterDTO> parameters) {
        this.taxName = taxName;
        this.methodName = methodName;
        convertReferenceAndPrediction(dates, referenceValues, predictionValues);
        this.mathErrors = mathErrors;
        this.parameters = parameters;
    }

    private void convertReferenceAndPrediction(List<Date> dates,
                                               List<Double> referenceValues,
                                               List<Double> predictionValues){
        this.referenceValues = new LinkedList<>();
        this.predictionValues = new LinkedList<>();
        for(int i=0; i<referenceValues.size(); ++i){
            this.referenceValues.add(new SeriesValueDTO(dates.get(i), referenceValues.get(i)));
        }
        for(int i=0; i<predictionValues.size(); ++i){
            this.predictionValues.add(new SeriesValueDTO(dates.get(i), predictionValues.get(i)));
        }
    }
}
