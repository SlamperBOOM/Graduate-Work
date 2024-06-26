package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxType;
import com.slamperboom.backend.dataLogic.views.predictions.PredictionValueView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxValueView;
import com.slamperboom.backend.exceptions.errorCodes.DataCodes;
import com.slamperboom.backend.exceptions.errorCodes.PredictionCodes;
import com.slamperboom.backend.exceptions.exceptions.FileParserException;
import com.slamperboom.backend.exceptions.exceptions.PredictionException;
import com.slamperboom.backend.mathematics.ImplementedEntitiesService;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.resultData.MathError;
import com.slamperboom.backend.mathematics.resultData.PredictionResult;
import com.slamperboom.backend.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        List<TaxValueView> taxValues = taxService.getValuesForTax(taxName);
        List<List<TaxValueView>> factorsValues = taxService.getFactorsForTax(taxName);
        List<Date> dates = new LinkedList<>();
        List<Double> reference = new LinkedList<>();
        List<List<Double>> factors = new LinkedList<>();

        if(taxValues.isEmpty()){
            throw new PredictionException(PredictionCodes.noValues);
        }
        List<Date> minimumDates = new LinkedList<>();
        List<Date> maximumDates = new LinkedList<>();
        minimumDates.add(taxValues.get(0).date());
        maximumDates.add(taxValues.get(taxValues.size()-1).date());
        if(!factorsValues.isEmpty()){
            minimumDates.addAll(factorsValues.stream().map(l -> l.get(0).date()).toList());
            maximumDates.addAll(factorsValues.stream().map(l -> l.get(l.size()-1).date()).toList());
        }
        //в каждом листе будет как минимум один элемент, поэтому минимум/максимум будет всегда
        Date minDate = minimumDates.stream().max(Date::compareTo).orElse(new Date(0));
        Date maxDate = maximumDates.stream().min(Date::compareTo).orElse(new Date(Long.MAX_VALUE));
        for (TaxValueView valueView : taxValues){
            if(valueView.date().compareTo(minDate) >= 0 && valueView.date().compareTo(maxDate) <= 0){
                reference.add(valueView.value());
                dates.add(valueView.date());
            }
        }
        for(List<TaxValueView> factorValues: factorsValues){
            List<Double> values = new LinkedList<>();
            for (TaxValueView factorView: factorValues){
                if(factorView.date().compareTo(minDate) >= 0 && factorView.date().compareTo(maxDate) <= 0){
                    values.add(factorView.value());
                }
            }
            factors.add(values);
        }
        return new AlgorithmValues(dates, reference, factors);
    }

    @Override
    public List<List<MathError>> getErrorsForTaxPredictions(String taxName){
        List<List<MathError>> errors = new LinkedList<>();
        for(String methodName: implementedEntitiesService.getImplementedAlgorithmsNames()){
            errors.add(predictionService.getErrorsForPrediction(taxName, methodName));
        }
        return errors;
    }

    @Override
    public List<PredictionResult> fetchResultsForTax(AlgorithmValues values, String taxName){
        List<PredictionResult> predictionResultDTOlist = new LinkedList<>();

        List<List<PredictionValueView>> predictions = new LinkedList<>();
        for(String methodName: implementedEntitiesService.getImplementedAlgorithmsNames()) {
            var result = predictionService.getPredictionForTaxAndMethod(taxName, methodName);
            if(!result.isEmpty()) {
                predictions.add(result);
            }
        }
        for(List<PredictionValueView> prediction: predictions){
            String methodName = prediction.get(0).methodName();
            List<Date> predictionDates = prediction.stream().map(PredictionValueView::date).toList();
            Date minimum = values.getDates().get(0).compareTo(predictionDates.get(0)) < 0 ? predictionDates.get(0) : values.getDates().get(0);
            List<Double> predictionValues = new LinkedList<>();
            List<Double> ref = new LinkedList<>();
            for(var predVal: prediction) {
                if(predVal.date().compareTo(minimum) >= 0) {
                    predictionValues.add(predVal.value());
                }
            }
            for(var refVal: taxService.getValuesForTax(taxName)) {
                if(refVal.date().compareTo(minimum) >= 0) {
                    ref.add(refVal.value());
                }
            }
            predictionResultDTOlist.add(PredictionResult.createInstanceFromRawWithoutErrors(taxName,
                    methodName, predictionDates, ref,
                    predictionValues, predictionService.getParametersForPrediction(taxName, methodName)));
        }
        return predictionResultDTOlist;
    }

    @Override
    public List<PredictionResult> getResultsForTax(String taxName){
        List<List<MathError>> errorsForAlgorithms = getErrorsForTaxPredictions(taxName);
        for(String currentError: implementedEntitiesService.getImplementedErrorsNames()){
            List<MathError> currentErrors = errorsForAlgorithms.stream().map(l ->
                    l.stream().filter(e -> e.getErrorName().equals(currentError)).findFirst().orElse(null)
            ).filter(Objects::nonNull).toList();
            if(currentErrors.isEmpty()){
                continue;
            }
            MathError bestError = currentErrors.get(0);
            for(MathError error: currentErrors){
                if(implementedEntitiesService.getMathErrorByName(error.getErrorName())
                        .compareTo(error.getValue(), bestError.getValue())){
                    bestError = error;
                }
            }
            MathError finalBestError = bestError;
            currentErrors.forEach(mathError -> mathError.setBetter(mathError == finalBestError));
        }

        List<PredictionResult> results = fetchResultsForTax(fetchValuesForAlgorithm(taxName), taxName);
        for(PredictionResult predictionResult : results){
            predictionResult.setMathErrors(errorsForAlgorithms.stream()
                    .filter(l -> !l.isEmpty() && l.get(0).getMethodName().equals(predictionResult.getMethodName()))
                    .findFirst().orElse(new LinkedList<>())
            );
        }
        return results;
    }

    @Override
    public void savePredictionResult(PredictionResult resultDTO) {
        predictionService.savePredictionResult(resultDTO.getTaxName(), resultDTO.getMethodName(), resultDTO.getPredictionValues());
        predictionService.saveErrorsForPrediction(resultDTO.getTaxName(), resultDTO.getMethodName(), resultDTO.getMathErrors());
        predictionService.saveParametersForPrediction(resultDTO.getTaxName(), resultDTO.getMethodName(), resultDTO.getParameters());
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
        Map<Date, Double> values = new HashMap<>();
        try {
            if(file.isEmpty()){
                throw new FileParserException(DataCodes.emptyFile);
            }
            String data = new String(file.getBytes());
            Scanner reader = new Scanner(data);

            reader.nextLine(); //assuming that first line contains header
            while(reader.hasNextLine()){
                String[] line = reader.nextLine().split(";");
                if(line.length < 2){
                    throw new FileParserException(DataCodes.wrongFileFormat);
                }
                values.put(
                        DateUtils.parseDateFromString(line[0]), Double.valueOf(line[1].replace(",", "."))
                );
            }
            reader.close();
        }catch (IOException e){
            throw new FileParserException(DataCodes.wrongFileFormat);
        }
        return values;
    }
}
