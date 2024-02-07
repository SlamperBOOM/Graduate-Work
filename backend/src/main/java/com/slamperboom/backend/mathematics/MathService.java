package com.slamperboom.backend.mathematics;

import com.slamperboom.backend.dataLogic.services.PredictionService;
import com.slamperboom.backend.dataLogic.services.TaxService;
import com.slamperboom.backend.dataLogic.views.predictions.PredictionView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxView;
import com.slamperboom.backend.frontendDTO.AlgorithmDTO;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmParameters;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.algorithms.PredictionAlgorithm;
import com.slamperboom.backend.mathematics.errors.MathError;
import com.slamperboom.backend.mathematics.results.MathErrorDTO;
import com.slamperboom.backend.mathematics.results.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MathService {
    private final Map<String, PredictionAlgorithm> implementedAlgorithms;
    private final Map<String, MathError> implementedErrors;
    private final TaxService taxService;
    private final PredictionService predictionService;

    @Autowired
    public MathService(List<PredictionAlgorithm> implementedAlgorithms,
                       List<MathError> implementedErrors,
                       TaxService taxService,
                       PredictionService predictionService) {
        this.implementedAlgorithms = new TreeMap<>(String::compareTo);
        for(PredictionAlgorithm algorithm : implementedAlgorithms){
            this.implementedAlgorithms.put(algorithm.getName(), algorithm);
        }
        this.implementedErrors = new TreeMap<>(String::compareTo);
        for(MathError mathError: implementedErrors){
            this.implementedErrors.put(mathError.getName(), mathError);
        }
        this.taxService = taxService;
        this.predictionService = predictionService;
    }

    public List<ResultDTO> makePrediction(String taxName, String methodName, List<String> params){
        //парсим параметры
        PredictionAlgorithm algorithm = getAlgorithm(methodName);
        AlgorithmParameters parameters = algorithm.getParameters();
        parameters.parseParameters(params);
        AlgorithmValues values = fetchValuesForAlgorithm(taxName);
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
        List<MathErrorDTO> predictionErrors = new ArrayList<>(implementedErrors.size());
        for(MathError mathError: implementedErrors.values()){
            predictionErrors.add(new MathErrorDTO(methodName, mathError.getName(), mathError.calcError(values.getReference(), prediction)));
        }
        //сравниваем с ошибками в других прогнозах
        List<List<MathErrorDTO>> errorOfOtherAlgorithms = getErrorForOtherAlgorithms(taxName);
        for(List<MathErrorDTO> listOfErrors: errorOfOtherAlgorithms){
            for(MathErrorDTO dto: listOfErrors){
                predictionErrors.stream().filter(e -> e.getErrorName().equals(dto.getErrorName())).findFirst()
                        .ifPresent(e ->
                                dto.setBetter(implementedErrors.get(dto.getErrorName()).compareTo(dto.getValue(), e.getValue()))
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
        List<ResultDTO> otherResults = fetchResultsForOtherAlgorithms(values, taxName);
        for(ResultDTO resultDTO: otherResults){
            resultDTO.setMathErrors(errorOfOtherAlgorithms.stream()
                    .filter(l -> !l.isEmpty() && l.get(0).getAlgorithmName().equals(resultDTO.getMethodName()))
                    .findFirst().orElse(new LinkedList<>())
            );
        }
        results.addAll(otherResults);
        //сохраняем результаты прогноза в бд: прогноз, ошибки, параметры
        predictionService.savePredictionResult(taxName, methodName, dates, prediction);
        predictionService.saveErrorsForPrediction(taxName, predictionErrors);
        if(!algorithm.getPredictionParameters().isEmpty()) {
            predictionService.saveParametersForPrediction(taxName, methodName, algorithm.getPredictionParameters());
        }
        return results;
    }

    public List<AlgorithmDTO> getMethods(){
        return implementedAlgorithms.values()
                .stream().map(a -> new AlgorithmDTO(a.getName(), a.getDescription(), a.getParameters().getParametersNames())).toList();
    }

    private PredictionAlgorithm getAlgorithm(String name){
        PredictionAlgorithm algorithm = implementedAlgorithms.get(name);
        if(algorithm == null){
            throw new NoSuchElementException();
        }
        return algorithm;
    }

    private List<ResultDTO> fetchResultsForOtherAlgorithms(AlgorithmValues values, String taxName){
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

    private List<List<MathErrorDTO>> getErrorForOtherAlgorithms(String taxName){
        List<List<MathErrorDTO>> errors = new LinkedList<>();
        for(String methodName: implementedAlgorithms.keySet()){
            errors.add(predictionService.getErrorsForPrediction(taxName, methodName));
        }
        return errors;
    }

    private AlgorithmValues fetchValuesForAlgorithm(String taxName){
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
}
