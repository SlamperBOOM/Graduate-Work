package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxType;
import com.slamperboom.backend.dataLogic.views.predictions.PredictionView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxView;
import com.slamperboom.backend.mathematics.ImplementedEntitiesService;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.results.MathErrorDTO;
import com.slamperboom.backend.mathematics.results.PredictionResultDTO;
import com.slamperboom.backend.mathematics.results.ResultParameterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DataService implements IMathDataService, IControllerDataService {
    private final TaxService taxService;
    private final PredictionService predictionService;
    private final ImplementedEntitiesService implementedEntitiesService;

    @Override
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

    @Override
    public List<List<MathErrorDTO>> getErrorsForTaxPredictions(String taxName){
        List<List<MathErrorDTO>> errors = new LinkedList<>();
        for(String methodName: implementedEntitiesService.getImplementedAlgorithmsNames()){
            errors.add(predictionService.getErrorsForPrediction(taxName, methodName));
        }
        return errors;
    }

    @Override
    public List<PredictionResultDTO> fetchResultsForTax(AlgorithmValues values, String taxName){
        List<PredictionResultDTO> predictionResultDTOlist = new LinkedList<>();

        List<List<PredictionView>> predictions = new LinkedList<>();
        for(String methodName: implementedEntitiesService.getImplementedAlgorithmsNames()) {
            var result = predictionService.getPredictionForTaxAndMethod(taxName, methodName);
            if(!result.isEmpty()) {
                predictions.add(result);
            }
        }
        for(List<PredictionView> prediction: predictions){
            String methodName = prediction.get(0).methodName();
            List<Date> dates = prediction.stream().map(PredictionView::date).toList();
            List<Double> predictionValues = prediction.stream().map(PredictionView::value).toList();
            predictionResultDTOlist.add(new PredictionResultDTO(taxName,
                    methodName, dates, values.getReference(),
                    predictionValues, predictionService.getParametersForPrediction(taxName, methodName)));
        }
        return predictionResultDTOlist;
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

    @Override
    public List<PredictionResultDTO> getResultsForTax(String taxName){
        List<List<MathErrorDTO>> errorsForAlgorithms = getErrorsForTaxPredictions(taxName);
        List<PredictionResultDTO> results = fetchResultsForTax(fetchValuesForAlgorithm(taxName), taxName);
        for(PredictionResultDTO predictionResultDTO : results){
            predictionResultDTO.setMathErrors(errorsForAlgorithms.stream()
                    .filter(l -> !l.isEmpty() && l.get(0).getMethodName().equals(predictionResultDTO.getMethodName()))
                    .findFirst().orElse(new LinkedList<>())
            );
        }
        return results;
    }

    @Override
    public void parseFileAndAddTaxValues(MultipartFile file, String taxName) {
        Map<Date, Double> values = parseValuesFromFile(file);
        List<TaxCreateView> views = new LinkedList<>();
        for(var entry: values.entrySet()){
            views.add(new TaxCreateView(taxName, TaxType.TAX, entry.getKey(), entry.getValue()));
        }
        taxService.addTaxValueViaList(views);
    }

    @Override
    public void parseFileAndAddFactorValues(MultipartFile file, String taxName) {
        Map<Date, Double> values = parseValuesFromFile(file);
        List<TaxCreateView> views = new LinkedList<>();
        for(var entry: values.entrySet()){
            views.add(new TaxCreateView(taxName, TaxType.FACTOR, entry.getKey(), entry.getValue()));
        }
        taxService.addTaxValueViaList(views);
    }

    private Map<Date, Double> parseValuesFromFile(MultipartFile file){
        //TODO сделать парсинг файлов с данными
        //Продумать формат файлов
        return new HashMap<>();
    }
}
