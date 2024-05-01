package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.dataLogic.entities.predictions.Prediction;
import com.slamperboom.backend.dataLogic.entities.predictions.PredictionError;
import com.slamperboom.backend.dataLogic.entities.taxes.Tax;
import com.slamperboom.backend.dataLogic.mappers.PredictionMapper;
import com.slamperboom.backend.dataLogic.repositories.prediction.PredictionErrorRepository;
import com.slamperboom.backend.dataLogic.repositories.prediction.PredictionParametersRepository;
import com.slamperboom.backend.dataLogic.repositories.prediction.PredictionValuesRepository;
import com.slamperboom.backend.dataLogic.repositories.prediction.PredictionsRepository;
import com.slamperboom.backend.dataLogic.views.predictions.PredictionValueView;
import com.slamperboom.backend.mathematics.resultData.MathError;
import com.slamperboom.backend.mathematics.resultData.ResultParameter;
import com.slamperboom.backend.mathematics.resultData.SeriesValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PredictionService {
    private final IPredictionTaxService taxService;
    private final PredictionsRepository predictionsRepository;
    private final PredictionValuesRepository predictionValuesRepository;
    private final PredictionMapper predictionMapper;
    private final PredictionParametersRepository predictionParametersRepository;
    private final PredictionErrorRepository predictionErrorRepository;

    private Prediction getIdByTaxAndMethod(String taxName, String methodName){
        Tax tax = taxService.getTaxByTaxName(taxName);
        if(tax != null){
            var prediction = predictionsRepository.findByTaxAndMethod(tax, methodName);
            return prediction.orElse(null);
        }else{
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<PredictionValueView> getPredictionForTaxAndMethod(String taxName, String methodName){
        Prediction prediction = getIdByTaxAndMethod(taxName, methodName);
        return new LinkedList<>(predictionValuesRepository.findByPrediction(prediction)
                .stream().map(predictionMapper::fromValueToView).toList());
    }

    @Transactional(readOnly = true)
    public List<ResultParameter> getParametersForPrediction(String taxName, String methodName){
        Prediction prediction = getIdByTaxAndMethod(taxName, methodName);
        return predictionParametersRepository.findByPrediction(prediction).stream().map(predictionMapper::fromParameterToDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<MathError> getErrorsForPrediction(String taxName, String methodName){
        Prediction prediction = getIdByTaxAndMethod(taxName, methodName);
        List<PredictionError> errors = new LinkedList<>(predictionErrorRepository.findByPrediction(prediction));
        return errors.stream().map(predictionMapper::fromErrorToDTO).toList();
    }

    public void savePredictionResult(String taxName, String methodName, List<SeriesValue> predictions){
        Tax tax = taxService.getTaxByTaxName(taxName);
        if(tax == null){
            return;
        }
        var prediction = predictionsRepository.findByTaxAndMethod(tax, methodName);
        if(prediction.isEmpty()){
            Prediction newPrediction = new Prediction();
            newPrediction.setTax(tax);
            newPrediction.setMethodName(methodName);
            predictionsRepository.save(newPrediction);
            predictionsRepository.flush();
            prediction = predictionsRepository.findByTaxAndMethod(tax, methodName);
        }else{
            predictionValuesRepository.deleteAll(predictionValuesRepository.findByPrediction(prediction.get()));
            predictionValuesRepository.flush();
        }
        Optional<Prediction> finalPrediction = prediction;
        predictionValuesRepository.saveAll(
                predictions.stream().map(value -> predictionMapper.fromSeriesToValue(value, finalPrediction.get())).toList()
        );
    }

    public void saveErrorsForPrediction(String taxName, String methodName, List<MathError> errors){
        Tax tax = taxService.getTaxByTaxName(taxName);
        var prediction = predictionsRepository.findByTaxAndMethod(tax, methodName).get();
        predictionErrorRepository.deleteAll(predictionErrorRepository.findByPrediction(prediction));
        predictionErrorRepository.flush();
        predictionErrorRepository.saveAll(errors.stream().map(error -> predictionMapper.fromDTOToError(error, prediction)).toList());
    }

    public void saveParametersForPrediction(String taxName, String methodName, List<ResultParameter> parameters) {
        Tax tax = taxService.getTaxByTaxName(taxName);
        var prediction = predictionsRepository.findByTaxAndMethod(tax, methodName).get();
        predictionParametersRepository.findByPrediction(prediction)
                .forEach(predictionParametersRepository::delete);
        predictionParametersRepository.flush();
        predictionParametersRepository.saveAll(predictionMapper.fromDTOToPredictionParameter(prediction, parameters));
    }
}
