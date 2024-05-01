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
import java.util.LinkedList;
import java.util.List;

@Component
public class PredictionMapper {

    public PredictionValueView fromValueToView(PredictionValue value){
        return new PredictionValueView(value.getPrediction().getMethodName(), value.getDate(), value.getValue());
    }

    public ResultParameter fromParameterToDTO(PredictionParameter parameter){
        return new ResultParameter(parameter.getParameterName(), parameter.getValue());
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

    public List<PredictionParameter> fromDTOToPredictionParameter(Prediction prediction, List<ResultParameter> resultParameters){
        List<PredictionParameter> parameters = new LinkedList<>();
        for(ResultParameter resultParameter: resultParameters){
            PredictionParameter param = new PredictionParameter();
            param.setPrediction(prediction);
            param.setParameterName(resultParameter.paramName());
            param.setValue(resultParameter.value());
            parameters.add(param);
        }
        return parameters;
    }
}
