package com.slamperboom.backend.mathematics;

import com.slamperboom.backend.dataLogic.services.IMathDataService;
import com.slamperboom.backend.exceptions.errorCodes.PredictionCodes;
import com.slamperboom.backend.exceptions.exceptions.PredictionException;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmParameters;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.algorithms.PredictionAlgorithm;
import com.slamperboom.backend.mathematics.resultData.MathError;
import com.slamperboom.backend.mathematics.resultData.PredictionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MathService {
    private final IMathDataService dataService;
    private final ImplementedEntitiesService implementedEntitiesService;

    public List<PredictionResult> makePrediction(String taxName, String methodName, List<String> params){
        //парсим параметры
        PredictionAlgorithm algorithm = implementedEntitiesService.getAlgorithmByName(methodName);
        AlgorithmParameters parameters = algorithm.getParameters();
        try {
            parameters.parseParameters(params);
        }catch (Exception e){
            throw new PredictionException(PredictionCodes.wrongParameterFormat);
        }
        AlgorithmValues values = dataService.fetchValuesForAlgorithm(taxName);
        //делаем прогноз
        List<Double> prediction;
        try {
            prediction = algorithm.makePrediction(values, parameters);
        }catch (Exception e){
            e.printStackTrace();
            throw new PredictionException(PredictionCodes.predictionError);
        }
        if(prediction.isEmpty()){
            throw new PredictionException(PredictionCodes.predictionError);
        }
        List<Date> dates = values.getDates();
        long timePeriod = dates.get(1).getTime() - dates.get(0).getTime();
        for (int i = 0; i < prediction.size() - values.getReference().size(); ++i) {
            dates.add(new Date(dates.get(dates.size()-1).getTime() + timePeriod));
        }
        //вычисляем ошибки
        List<MathError> predictionErrors = new LinkedList<>();
        for(com.slamperboom.backend.mathematics.mathErrors.MathError mathError: implementedEntitiesService.getImplementedErrors()){
            predictionErrors.add(new MathError(methodName, mathError.getName(), mathError.calcError(values.getReference(), prediction)));
        }
        //сравниваем с ошибками в других прогнозах
        List<List<MathError>> errorOfOtherAlgorithms = dataService.getErrorsForTaxPredictions(taxName);
        for(List<MathError> listOfErrors: errorOfOtherAlgorithms){
            for(MathError dto: listOfErrors){
                predictionErrors.stream().filter(e -> e.getErrorName().equals(dto.getErrorName())).findFirst()
                        .ifPresent(e ->
                                dto.setBetter(implementedEntitiesService.getMathErrorByName(dto.getErrorName())
                                        .compareTo(dto.getValue(), e.getValue()))
                        );
            }
        }
        //собираем ResultDTOs
        List<PredictionResult> results = new ArrayList<>();
        results.add(PredictionResult.createInstanceFromRawWithErrors(taxName,
                methodName, dates, values.getReference(),
                prediction, predictionErrors, algorithm.getPredictionParameters()));
        List<PredictionResult> otherResults = dataService.fetchResultsForTax(values, taxName);
        for(PredictionResult predictionResult : otherResults){
            predictionResult.setMathErrors(errorOfOtherAlgorithms.stream()
                    .filter(l -> !l.isEmpty() && l.get(0).getMethodName().equals(predictionResult.getMethodName()))
                    .findFirst().orElse(new LinkedList<>())
            );
        }
        results.addAll(otherResults);
        return results;
    }
}
