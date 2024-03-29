package com.slamperboom.backend.mathematics.algorithms.predictionAlgorithms;

import com.slamperboom.backend.exceptions.errorCodes.PredictionCodes;
import com.slamperboom.backend.exceptions.exceptions.PredictionException;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmParameters;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.algorithms.PredictionAlgorithm;
import com.slamperboom.backend.mathematics.resultData.ResultParameter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ARIMA implements PredictionAlgorithm {
    private static final String methodName = "ARIMA";
    private int bestP;
    private int bestD;
    private int bestQ;

    @Override
    public List<Double> makePrediction(AlgorithmValues referenceValues, AlgorithmParameters parameters) {
        List<Integer> pParams = parameters.getParameterValues("p").stream().map(Double::intValue).toList();
        List<Integer> dParams = parameters.getParameterValues("d").stream().map(Double::intValue).toList();
        int qParam = parameters.getParameterValues("q").get(0).intValue();
        List<Double> values = referenceValues.getReference();
        double bestMSE = Double.MAX_VALUE;
        List<Double> bestPredict = new LinkedList<>();

        //Определять параметр d и вычислять разности

        //Вычислять коэффициенты для модели AR и считать AR часть

        //Вычислять MA часть

        return bestPredict;
    }

    private double[] inverseList(List<Double> values){
        double[] array = new double[values.size()];
        for(int i=0; i<values.size(); ++i){
            array[i] = values.get(values.size()-1-i);
        }
        return array;
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    public AlgorithmParameters getParameters() {
        return new ARIMAParameters();
    }

    @Override
    public String getDescription() {
        return """
                Алгоритм ARIMA, состоящий из 3 моделей:
                1. AR - авторегрессия
                2. I - интегрирующая
                3. MA - скользящее среднее
                """;
    }

    @Override
    public List<ResultParameter> getPredictionParameters() {
        List<ResultParameter> parameters = new LinkedList<>();
        parameters.add(new ResultParameter("P", (double) bestP));
        parameters.add(new ResultParameter("D", (double) bestD));
        parameters.add(new ResultParameter("Q", (double) bestQ));
        return parameters;
    }

    private static class ARIMAParameters implements AlgorithmParameters{
        private static final List<String> paramsNames = List.of(
                "P - порядок авторегрессии (можно перечислить несколько значений через ';')",
                "D - порядок интегрирования (можно перечислить несколько значений через ';')",
                "Q - порядок скользящего среднего");
        private List<Double> paramP;
        private List<Double> paramD;
        private List<Double> paramQ;

        @Override
        public List<String> getParametersNames() {
            return paramsNames;
        }

        @Override
        public void parseParameters(List<String> stringParams) {
            try {
                paramP = Arrays.stream(stringParams.get(0).split(";")).map(Double::parseDouble).sorted().toList();
                paramD = Arrays.stream(stringParams.get(1).split(";")).map(Double::parseDouble).sorted().toList();
                paramQ = List.of(Double.parseDouble(stringParams.get(2)));
            }catch (NumberFormatException e){
                throw new PredictionException(PredictionCodes.wrongParameterFormat);
            }
        }

        @Override
        public List<Double> getParameterValues(String paramName) {
            switch (paramName){
                case "p" -> {return paramP;}
                case "d" -> {return paramD;}
                case "q" -> {return paramQ;}
                default -> {return Collections.emptyList();}
            }
        }
    }

    private static class NeuralNet {
        private final List<List<Double>> factors;
        private final List<Double> references;

        public NeuralNet(List<Double> referenceValues, List<List<Double>> factors){
            if(factors == null || referenceValues == null){
                throw new ArrayStoreException("No lists were passed");
            }
            if(factors.size() != referenceValues.size()){
                throw new ArrayStoreException("List sizes are different");
            }
            this.factors = factors;
            this.references = referenceValues;
        }

        public double[] linearRegression(double learningRate, int epochs){
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
            return weights;
        }

        public double calcNewValueLinear(List<Double> factorList, double[] weights){
            double value = 0;
            for(int i=0; i<factorList.size(); ++i){
                value += weights[i]*factorList.get(i);
            }
            return value;
        }

        private double sigmoid(double x){
            return 1.0/(1+Math.pow(Math.E, -x/5000));
        }

        private double sigmoidDerivative(double x){
            double sig = sigmoid(x);
            return sig*(1-sig);
        }

        private double outputError(double expected, double predicted){
            return Math.signum(expected - predicted)*Math.pow(Math.abs(expected - predicted), 1.0/5);
        }
    }
}
