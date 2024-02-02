package com.slamperboom.backend.mathematics;

import com.slamperboom.backend.mathematics.algorithms.AlgorithmParameters;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.algorithms.PrecisionAlgorithm;
import com.slamperboom.backend.mathematics.errors.MathError;
import com.slamperboom.backend.mathematics.results.MathErrorDTO;
import com.slamperboom.backend.mathematics.results.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MathService {
    @Autowired
    private List<PrecisionAlgorithm> implementedAlgorithms;
    @Autowired
    private List<MathError> implementedErrors;

    public List<ResultDTO> makePrecision(String taxName, String methodName, List<String> params){
        //парсим параметры
        PrecisionAlgorithm algorithm = getAlgorithm(methodName);
        AlgorithmParameters parameters = algorithm.getParameters();
        parameters.parseParameters(params);
        //запрос к бд на получение данных для прогноза

        //делаем прогноз
        List<Double> reference = new ArrayList<>();
        List<Double> prediction = algorithm.makePrecision(new AlgorithmValues(new ArrayList<>(), new ArrayList<>()), parameters);
        //вычисляем ошибки
        List<MathErrorDTO> predictionErrors = new ArrayList<>(implementedErrors.size());
        for(MathError mathError: implementedErrors){
            predictionErrors.add(new MathErrorDTO(mathError.getName(), mathError.calcError(reference, prediction)));
        }
        //сравниваем с ошибками в других прогнозах

        //собираем ResultDTO
        List<ResultDTO> results = new ArrayList<>();
        return new ArrayList<>();
    }

    private PrecisionAlgorithm getAlgorithm(String name){
        Optional<PrecisionAlgorithm> algorithmOptional = implementedAlgorithms.stream().filter((v) -> v.getName().equals(name)).findFirst();
        if(algorithmOptional.isEmpty()){
            throw new NoSuchElementException();
        }
        return algorithmOptional.get();
    }
}
