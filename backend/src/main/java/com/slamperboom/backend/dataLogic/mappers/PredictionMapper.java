package com.slamperboom.backend.dataLogic.mappers;

import com.slamperboom.backend.dataLogic.entities.predictions.Prediction;
import com.slamperboom.backend.dataLogic.entities.predictions.PredictionError;
import com.slamperboom.backend.dataLogic.entities.predictions.PredictionParameter;
import com.slamperboom.backend.dataLogic.entities.predictions.PredictionValue;
import com.slamperboom.backend.dataLogic.views.predictions.PredictionValueView;
import com.slamperboom.backend.mathematics.resultData.MathError;
import com.slamperboom.backend.mathematics.resultData.ResultParameter;
import com.slamperboom.backend.mathematics.resultData.SeriesValue;
import com.slamperboom.backend.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class PredictionMapper {

    public PredictionValueView fromValueToView(PredictionValue value){
        return new PredictionValueView(value.getPrediction().getMethodName(), value.getDate(), value.getValue());
    }

    public List<ResultParameter> fromParameterToDTO(String parameter){
        if(parameter.isEmpty()){
            return Collections.emptyList();
        }else {
            return Arrays.stream(parameter.split(";")).map(param -> {
                String[] split = param.split("=");
                return new ResultParameter(split[0], Double.parseDouble(split[1]));
            }).toList();
        }
    }

    public MathError fromErrorToDTO(PredictionError error){
        return new MathError(error.getPrediction().getMethodName(), error.getErrorName(), error.getValue());
    }

    public PredictionError fromDTOToError(MathError error, Prediction prediction){
        PredictionError predictionError = new PredictionError();
        predictionError.setPrediction(prediction);
        predictionError.setErrorName(error.getErrorName());
        predictionError.setValue(error.getValue());
        return predictionError;
    }

    public PredictionValue fromSeriesToValue(SeriesValue value, Prediction prediction){
        PredictionValue predictionValue = new PredictionValue();
        predictionValue.setPrediction(prediction);
        predictionValue.setDate(DateUtils.parseDateFromString(value.date()));
        predictionValue.setValue(value.value());
        return predictionValue;
    }

    public PredictionParameter fromDTOToPredictionParameter(Prediction prediction, List<ResultParameter> resultParameters){
        PredictionParameter parameter = new PredictionParameter();
        parameter.setPrediction(prediction);
        StringBuilder builder = new StringBuilder(255);
        for(ResultParameter resultParameter: resultParameters){
            builder.append(resultParameter.paramName()).append("=").append(resultParameter.value()).append(";");
        }
        parameter.setParameters(builder.toString());
        return parameter;
    }
}
