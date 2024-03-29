package com.slamperboom.backend.mathematics.algorithms.predictionAlgorithms;

import com.slamperboom.backend.mathematics.algorithms.AlgorithmParameters;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.algorithms.PredictionAlgorithm;
import com.slamperboom.backend.mathematics.resultData.ResultParameter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SSA implements PredictionAlgorithm {
    private static final String methodName = "SSA";

    @Override
    public List<Double> makePrediction(AlgorithmValues referenceValues, AlgorithmParameters parameters) {
        //Реализовать алгоритм
        return null;
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    public AlgorithmParameters getParameters() {
        return null;
    }

    @Override
    public String getDescription() {
        return PredictionAlgorithm.super.getDescription();
    }

    @Override
    public List<ResultParameter> getPredictionParameters() {
        return PredictionAlgorithm.super.getPredictionParameters();
    }

    private static class SSAParameters implements AlgorithmParameters{
        private static final List<String> paramsNames = List.of(
                "P - порядок авторегрессии (можно перечислить несколько значений через ';')",
                "D - порядок интегрирования (можно перечислить несколько значений через ';')",
                "Q - порядок скользящего среднего");

        @Override
        public List<String> getParametersNames() {
            return paramsNames;
        }

        @Override
        public void parseParameters(List<String> stringParams) {

        }

        @Override
        public List<Double> getParameterValues(String paramName) {
            return AlgorithmParameters.super.getParameterValues(paramName);
        }
    }
}
