package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.dataLogic.entities.predictions.PredictionError;
import com.slamperboom.backend.dataLogic.mappers.PredictionMapper;
import com.slamperboom.backend.dataLogic.repositories.PredictionErrorRepository;
import com.slamperboom.backend.dataLogic.repositories.PredictionParametersRepository;
import com.slamperboom.backend.dataLogic.repositories.PredictionsRepository;
import com.slamperboom.backend.dataLogic.views.predictions.PredictionView;
import com.slamperboom.backend.mathematics.resultData.MathError;
import com.slamperboom.backend.mathematics.resultData.ResultParameter;
import com.slamperboom.backend.mathematics.resultData.SeriesValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PredictionService {
    private final PredictionsRepository predictionsRepository;
    private final PredictionMapper predictionMapper;
    private final PredictionParametersRepository predictionParametersRepository;
    private final PredictionErrorRepository predictionErrorRepository;

    @Transactional(readOnly = true)
    public List<PredictionView> getPredictionForTaxAndMethod(String taxName, String methodName){
        return predictionsRepository.findByTaxAndMethod(taxName, methodName)
                .stream().map(predictionMapper::fromPredictionToView).toList();
    }

    @Transactional(readOnly = true)
    public List<ResultParameter> getParametersForPrediction(String taxName, String methodName){
        Optional<String> result = predictionParametersRepository.findParamByTaxAndMethod(taxName, methodName);
        if(result.isPresent()){
            return predictionMapper.fromParameterToDTO(result.get());

        }else{
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public List<MathError> getErrorsForPrediction(String taxName, String methodName){
        List<PredictionError> errors = predictionErrorRepository.findByTaxAndMethod(taxName, methodName);
        return errors.stream().map(predictionMapper::fromPredictionErrorToDTO).toList();
    }

    public void savePredictionResult(String taxName, String methodName, List<SeriesValue> predictions){
        List<PredictionView> values = new LinkedList<>();
        for (SeriesValue prediction : predictions) {
            values.add(new PredictionView(taxName, methodName, prediction.date(), prediction.value()));
        }
        predictionsRepository.deleteAll(predictionsRepository.findByTaxAndMethod(taxName, methodName));
        predictionsRepository.flush();
        predictionsRepository.saveAll(values.stream().map(predictionMapper::fromViewToPrediction).toList());
    }

    public void saveErrorsForPrediction(String taxName, List<MathError> errors){
        List<PredictionError> predictionErrors = new LinkedList<>();
        for(MathError errorDTO: errors){
            predictionErrors.add(predictionMapper.fromDTOToPredictionError(taxName, errorDTO));
        }
        predictionErrorRepository.deleteAll(predictionErrorRepository.findByTaxAndMethod(taxName, errors.get(0).getMethodName()));
        predictionErrorRepository.flush();
        predictionErrorRepository.saveAll(predictionErrors);
    }

    public void saveParametersForPrediction(String taxName, String methodName, List<ResultParameter> parameters) {
        predictionParametersRepository.findByTaxAndMethod(taxName, methodName)
                .ifPresent(predictionParametersRepository::delete);
        predictionParametersRepository.flush();
        predictionParametersRepository.save(predictionMapper.fromDTOToPredictionParameter(taxName, methodName, parameters));
    }
}
