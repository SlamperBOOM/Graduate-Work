package com.slamperboom.backend.mathematics.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LinearRegression implements PrecisionAlgorithm{
    private static final String methodName = "Double Approximation";
    @Override
    public List<Double> makePrecision(AlgorithmValues referenceValues, AlgorithmParameters parameters) {
        //надо вычислять коэффы для всех вариантов "кол-во эпох"+"коэфф"
        //сохранить наилучшие коэффициенты
        List<List<Double>> factors = referenceValues.getFactors();
        double[] weights = new double[factors.get(0).size()];
        int factorsCount = factors.get(0).size();
        for(int i=0; i<weights.length; ++i){
            weights[i] = Math.random();
        }
        for(int k=0; k<epochs; ++k){
            for(int i=0; i<references.size(); ++i){//main body

                double referenceValue = sigmoid(references.get(i));
                List<Double> currentFactors = new ArrayList<>(factors.get(i));
                double predicted = 0;
                for(int j = 0; j< factorsCount; ++j){
                    predicted += currentFactors.get(j)*weights[j];
                }
                double error = outputError(referenceValue, sigmoid(predicted));
                for(int j=0; j< factorsCount; ++j){
                    double adding = error * sigmoidDerivative(predicted) * currentFactors.get(j) * learningRate;
                    weights[j] += adding;
                }
            }
        }
        //сделать прогноз факторов с помощью ARIMA
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    public AlgorithmParameters getParameters() {
        return new LinearRegressionParameters();
    }

    @Override
    public String getDescription() {
        return """
                
                """;
    }

    private static class LinearRegressionParameters implements AlgorithmParameters{
        private static final List<String> paramsNames = List.of("Коэффициент обучения (можно перечислить несколько значений через ';')",
                "Количество эпох обучения (можно перечислить несколько значений через ';')");
        private List<Double> learningRateVariants;
        private List<Double> epochs;

        @Override
        public List<String> getParametersNames() {
            return paramsNames;
        }

        @Override
        public void parseParameters(List<String> stringParams) {
            learningRateVariants = Arrays.stream(stringParams.get(0).split(";")).mapToDouble(Double::parseDouble).collect();
        }

        @Override
        public List<Double> getParameter(String paramName) {
            switch (paramName){
                case "learningRate" -> {
                    return learningRateVariants;
                }
                case "epochs" -> {
                    return epochs;
                }
                default -> {
                    return Collections.emptyList();
                }
            }

        }
    }
}
