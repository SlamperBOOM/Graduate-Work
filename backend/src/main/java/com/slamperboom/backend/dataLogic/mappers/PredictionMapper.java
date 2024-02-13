package com.slamperboom.backend.dataLogic.mappers;

import com.slamperboom.backend.dataLogic.entities.predictions.Prediction;
import com.slamperboom.backend.dataLogic.entities.predictions.PredictionError;
import com.slamperboom.backend.dataLogic.entities.predictions.PredictionParameter;
import com.slamperboom.backend.dataLogic.views.predictions.PredictionView;
import com.slamperboom.backend.mathematics.results.MathErrorDTO;
import com.slamperboom.backend.mathematics.results.ResultParameterDTO;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class PredictionMapper {

    public PredictionView fromPredictionToView(Prediction prediction){
        return new PredictionView(prediction.getTaxName(), prediction.getMethodName(), prediction.getDate(), prediction.getValue());
    }

    public Prediction fromViewToPrediction(PredictionView view){
        Prediction prediction = new Prediction();
        prediction.setTaxName(view.taxName());
        prediction.setMethodName(view.methodName());
        prediction.setDate(view.date());
        prediction.setValue(view.value());
        return prediction;
    }

    public List<ResultParameterDTO> fromParameterToDTO(String parameter){
        if(parameter.isEmpty()){
            return Collections.emptyList();
        }else {
            return Arrays.stream(parameter.split(";")).map(param -> {
                String[] split = param.split("=");
                return new ResultParameterDTO(split[0], Double.parseDouble(split[1]));
            }).toList();
        }
    }

    public MathErrorDTO fromPredictionErrorToDTO(PredictionError error){
        return new MathErrorDTO(error.getMethodName(), error.getErrorName(), error.getValue());
    }

    public PredictionError fromDTOToPredictionError(String taxName, MathErrorDTO errorDTO){
        PredictionError error = new PredictionError();
        error.setTaxName(taxName);
        error.setMethodName(errorDTO.getMethodName());
        error.setErrorName(errorDTO.getErrorName());
        error.setValue(errorDTO.getValue());
        return error;
    }

    public PredictionParameter fromDTOToPredictionParameter(String taxName,
                                                            String methodName,
                                                            List<ResultParameterDTO> parameters){
        PredictionParameter predictionParameter = new PredictionParameter();
        predictionParameter.setTaxName(taxName);
        predictionParameter.setMethodName(methodName);

        StringBuilder builder = new StringBuilder();
        for(ResultParameterDTO parameterDTO: parameters){
            builder.append(parameterDTO.paramName()).append("=").append(parameterDTO.value()).append(";");
        }
        predictionParameter.setParameters(builder.toString());
        return predictionParameter;
    }
}
