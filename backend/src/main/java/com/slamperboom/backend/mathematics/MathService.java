package com.slamperboom.backend.mathematics;

import com.slamperboom.backend.dataLogic.services.DataService;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmParameters;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.algorithms.ImplementedEntitiesService;
import com.slamperboom.backend.mathematics.algorithms.PredictionAlgorithm;
import com.slamperboom.backend.mathematics.errors.MathError;
import com.slamperboom.backend.mathematics.results.MathErrorDTO;
import com.slamperboom.backend.mathematics.results.ResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MathService {
    private final DataService dataService;
    private final ImplementedEntitiesService implementedEntitiesService;

    public List<ResultDTO> makePrediction(String taxName, String methodName, List<String> params){
        //парсим параметры
        PredictionAlgorithm algorithm = implementedEntitiesService.getAlgorithmByName(methodName);
        AlgorithmParameters parameters = algorithm.getParameters();
        parameters.parseParameters(params);
        AlgorithmValues values = dataService.fetchValuesForAlgorithm(taxName);
        //делаем прогноз
        List<Double> prediction = algorithm.makePrediction(values, parameters);
        if(prediction.isEmpty()){
            return Collections.emptyList();
        }
        List<Date> dates = values.getDates();
        long timePeriod = dates.get(1).getTime() - dates.get(0).getTime();
        for (int i = 0; i < prediction.size() - values.getReference().size(); ++i) {
            dates.add(new Date(dates.get(dates.size()-1).getTime() + timePeriod));
        }
        //вычисляем ошибки
        List<MathErrorDTO> predictionErrors = new LinkedList<>();
        for(MathError mathError: implementedEntitiesService.getImplementedErrors()){
            predictionErrors.add(new MathErrorDTO(methodName, mathError.getName(), mathError.calcError(values.getReference(), prediction)));
        }
        //сравниваем с ошибками в других прогнозах
        List<List<MathErrorDTO>> errorOfOtherAlgorithms = dataService.getErrorsForTaxPredictions(taxName);
        for(List<MathErrorDTO> listOfErrors: errorOfOtherAlgorithms){
            for(MathErrorDTO dto: listOfErrors){
                predictionErrors.stream().filter(e -> e.getErrorName().equals(dto.getErrorName())).findFirst()
                        .ifPresent(e ->
                                dto.setBetter(implementedEntitiesService.getMathErrorByName(dto.getErrorName())
                                        .compareTo(dto.getValue(), e.getValue()))
                        );
            }
        }
        System.out.println(predictionErrors);
        System.out.println(errorOfOtherAlgorithms);
        //собираем ResultDTOs
        List<ResultDTO> results = new ArrayList<>();
        results.add(new ResultDTO(taxName,
                methodName, values.getDates(), values.getReference(),
                prediction, predictionErrors, algorithm.getPredictionParameters()));
        List<ResultDTO> otherResults = dataService.fetchResultsForTax(values, taxName);
        for(ResultDTO resultDTO: otherResults){
            resultDTO.setMathErrors(errorOfOtherAlgorithms.stream()
                    .filter(l -> !l.isEmpty() && l.get(0).getAlgorithmName().equals(resultDTO.getMethodName()))
                    .findFirst().orElse(new LinkedList<>())
            );
        }
        results.addAll(otherResults);
        //сохраняем результаты прогноза в бд: прогноз, ошибки, параметры
        dataService.savePredictionResult(taxName, methodName, dates, prediction, predictionErrors, algorithm.getPredictionParameters());
        return results;
    }
}
