package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.dataLogic.views.predictions.PredictionView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxView;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.algorithms.PredictionAlgorithm;
import com.slamperboom.backend.mathematics.errors.MathError;
import com.slamperboom.backend.mathematics.results.MathErrorDTO;
import com.slamperboom.backend.mathematics.results.ResultDTO;
import com.slamperboom.backend.mathematics.results.ResultParameterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DataService {
    private final TaxService taxService;
    private final PredictionService predictionService;
    private final Map<String, PredictionAlgorithm> implementedAlgorithms;
    private final Map<String, MathError> implementedErrors;

    public AlgorithmValues fetchValuesForAlgorithm(String taxName){
        //достаем все значения для налога и его факторов (если есть) и обрезаем до минимального общего диапазона дат
        List<TaxView> taxValues = taxService.getValuesForTax(taxName);
        List<List<TaxView>> factorsValues = taxService.getFactorsForTax(taxName);
        List<Date> dates = new LinkedList<>();
        List<Double> reference = new LinkedList<>();
        List<List<Double>> factors = new LinkedList<>();

        if(taxValues.isEmpty()){
            throw new NoSuchElementException();
        }
        List<Date> minimumDates = new LinkedList<>();
        List<Date> maximumDates = new LinkedList<>();
        minimumDates.add(taxValues.get(0).getDate());
        maximumDates.add(taxValues.get(taxValues.size()-1).getDate());
        if(!factorsValues.isEmpty()){
            minimumDates.addAll(factorsValues.stream().map(l -> l.get(0).getDate()).toList());
            maximumDates.addAll(factorsValues.stream().map(l -> l.get(l.size()-1).getDate()).toList());
        }
        //в каждом листе будет как минимум один элемент, поэтому минимум/максимум будет всегда
        Date minDate = minimumDates.stream().max(Date::compareTo).orElseThrow();
        Date maxDate = maximumDates.stream().min(Date::compareTo).orElseThrow();
        for (TaxView taxView: taxValues){
            if(taxView.getDate().compareTo(minDate) >= 0 && taxView.getDate().compareTo(maxDate) <= 0){
                reference.add(taxView.getValue());
                dates.add(taxView.getDate());
            }
        }
        for(List<TaxView> factorValues: factorsValues){
            List<Double> values = new LinkedList<>();
            for (TaxView factorView: factorValues){
                if(factorView.getDate().compareTo(minDate) >= 0 && factorView.getDate().compareTo(maxDate) <= 0){
                    values.add(factorView.getValue());
                }
            }
            factors.add(values);
        }
        return new AlgorithmValues(dates, reference, factors);
    }

    public List<List<MathErrorDTO>> getErrorsForTaxPredictions(String taxName){
        List<List<MathErrorDTO>> errors = new LinkedList<>();
        for(String methodName: implementedAlgorithms.keySet()){
            errors.add(predictionService.getErrorsForPrediction(taxName, methodName));
        }
        return errors;
    }

    public List<ResultDTO> getResultsForTax(String taxName){
        List<List<MathErrorDTO>> errorsForAlgorithms = getErrorsForTaxPredictions(taxName);
        List<ResultDTO> results = fetchResultsForTax(fetchValuesForAlgorithm(taxName), taxName);
        for(ResultDTO resultDTO: results){
            resultDTO.setMathErrors(errorsForAlgorithms.stream()
                    .filter(l -> !l.isEmpty() && l.get(0).getAlgorithmName().equals(resultDTO.getMethodName()))
                    .findFirst().orElse(new LinkedList<>())
            );
        }
        return results;
    }

    public List<ResultDTO> fetchResultsForTax(AlgorithmValues values, String taxName){
        List<ResultDTO> resultDTOlist = new LinkedList<>();

        List<List<PredictionView>> predictions = new LinkedList<>();
        for(String methodName: implementedAlgorithms.keySet()) {
            var result = predictionService.getPredictionForTaxAndMethod(taxName, methodName);
            if(!result.isEmpty()) {
                predictions.add(result);
            }
        }
        for(List<PredictionView> prediction: predictions){
            String methodName = prediction.get(0).methodName();
            List<Date> dates = prediction.stream().map(PredictionView::date).toList();
            List<Double> predictionValues = prediction.stream().map(PredictionView::value).toList();
            resultDTOlist.add(new ResultDTO(taxName,
                    methodName, dates, values.getReference(),
                    predictionValues, predictionService.getParametersForPrediction(taxName, methodName)));
        }
        return resultDTOlist;
    }

    public void savePredictionResult(String taxName,
                                     String methodName,
                                     List<Date> dates,
                                     List<Double> prediction,
                                     List<MathErrorDTO> predictionErrors,
                                     List<ResultParameterDTO> parameters){
        predictionService.savePredictionResult(taxName, methodName, dates, prediction);
        predictionService.saveErrorsForPrediction(taxName, predictionErrors);
        predictionService.saveParametersForPrediction(taxName, methodName, parameters);
    }
}
