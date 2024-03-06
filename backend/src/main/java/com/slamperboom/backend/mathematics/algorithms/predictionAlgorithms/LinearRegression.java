package com.slamperboom.backend.mathematics.algorithms.predictionAlgorithms;

import com.slamperboom.backend.mathematics.algorithms.AlgorithmParameters;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.algorithms.PredictionAlgorithm;
import com.slamperboom.backend.mathematics.mathErrors.MRSEError;
import com.slamperboom.backend.mathematics.mathErrors.MathError;
import com.slamperboom.backend.mathematics.resultData.ResultParameter;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LinearRegression implements PredictionAlgorithm {
    private static final String methodName = "Linear Regression";
    private double[] bestWeights;
    private double bestLR;
    private double bestEpochs;

    @Override
    public List<Double> makePrediction(AlgorithmValues referenceValues, AlgorithmParameters parameters) {
        List<List<Double>> factors = referenceValues.getFactors();
        List<Double> references = referenceValues.getReference();
        System.out.println(references);
        System.out.println(factors);
        int factorsCount = factors.size();
        if(factorsCount == 0){
            return Collections.emptyList();
        }
        RealMatrix matr = new Array2DRowRealMatrix(factorsCount, references.size());
        for(int i=0; i<factorsCount; ++i) {
            matr.setRowVector(i, new ArrayRealVector(factors.get(i).toArray(Double[]::new)));
        }
        matr = matr.transpose();
        List<List<Double>> factorsTranspose = new LinkedList<>();
        for(double[] arr: matr.getData()){
            factorsTranspose.add(Arrays.stream(arr).boxed().toList());
        }
        System.out.println(factorsTranspose);
        double[] weights = new double[factorsCount];
        double bestError = Double.MAX_VALUE;
        MathError mathError = new MRSEError();
        double sigmoidParam = parameters.getParameter("sigmoidParam").get(0);
        double outputErrorParam = parameters.getParameter("outputErrorParam").get(0);
        for(double epochs : parameters.getParameter("epochs")){
            for(double learningRate: parameters.getParameter("learningRate")) {

                for(int i=0; i<weights.length; ++i){
                    weights[i] = Math.random();
                }
                for (int k = 0; k < epochs; ++k) {
                    for (int i = 0; i < references.size(); ++i) {//main body
                        double referenceValue = sigmoid(references.get(i), sigmoidParam);
                        List<Double> currentFactors = new ArrayList<>(factorsTranspose.get(i));
                        double predicted = 0;
                        for (int j = 0; j < factorsCount; ++j) {
                            predicted += currentFactors.get(j) * weights[j];
                        }
                        double error = outputError(referenceValue, sigmoid(predicted, sigmoidParam), outputErrorParam);
                        for (int j = 0; j < factorsCount; ++j) {
                            double adding = error * sigmoidDerivative(predicted, sigmoidParam) * currentFactors.get(j) * learningRate;
                            weights[j] += adding;
                        }
                    }
                }
                //сделаем приближение с помощью полученных коэффициентов
                List<Double> predicted = new ArrayList<>();
                for(List<Double> factorList: factorsTranspose){
                    predicted.add(calcNewValueLinear(factorList, weights));
                }
                double error = mathError.calcError(references, predicted);
                if(error < bestError){
                    bestWeights = weights;
                    bestEpochs = epochs;
                    bestLR = learningRate;
                    bestError = error;
                }
            }
        }
        PredictionAlgorithm doubleApproximation = new DoubleApproximation();
        List<Double> newFactors = new LinkedList<>();
        for(List<Double> factorList: factors) {
            AlgorithmValues values = new AlgorithmValues(factorList);
            var prediction = doubleApproximation.makePrediction(values, doubleApproximation.getParameters());
            newFactors.add(prediction.get(prediction.size()-1));
        }
        factorsTranspose.add(newFactors);
        List<Double> predicted = new ArrayList<>();
        for(List<Double> factorList: factorsTranspose){
            predicted.add(calcNewValueLinear(factorList, bestWeights));
        }
        return predicted;
    }

    private double sigmoid(double x, double xParam){
        return 1.0/(1+Math.pow(Math.E, -x/xParam));
    }

    private double sigmoidDerivative(double x, double xParam){
        double sig = sigmoid(x, xParam);
        return sig*(1-sig);
    }

    private double outputError(double expected, double predicted, double outputErrorParam){
        return Math.signum(expected - predicted)*Math.pow(Math.abs(expected - predicted), outputErrorParam);
    }

    private double calcNewValueLinear(List<Double> factorList, double[] weights){
        double value = 0;
        for(int i=0; i<factorList.size(); ++i){
            value += weights[i]*factorList.get(i);
        }
        return value;
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
                Множественная линейная регрессия с поиском коэффициентов при помощи нейронной сети.
                Примечание: Работа алгоритма может быть очень долгой.
                В результатах прогноза weight(i) соответствует i-му фактору, указанному в таблице "связанные факторы".
                """;
    }

    @Override
    public List<ResultParameter> getPredictionParameters() {
        List<ResultParameter> parameters = new LinkedList<>();
        parameters.add(new ResultParameter("Learning rate", bestLR));
        parameters.add(new ResultParameter("Epochs", bestEpochs));
        for(int i=0; i<bestWeights.length; ++i){
            parameters.add(new ResultParameter("weight"+(i+1), bestWeights[i]));
        }
        return parameters;
    }

    private static class LinearRegressionParameters implements AlgorithmParameters{
        private static final List<String> paramsNames = List.of("Коэффициент обучения (можно перечислить несколько значений через ';')",
                "Количество эпох обучения (можно перечислить несколько значений через ';')",
                "Параметр для сигмоиды при экспоненте (больше 1)",
                "Степень взятия корня при вычислении ошибки в обучении НС (больше 0)");
        private List<Double> learningRateVariants;
        private List<Double> epochs;
        private List<Double> sigmoidParam;
        private List<Double> outputErrorParam;

        @Override
        public List<String> getParametersNames() {
            return paramsNames;
        }

        @Override
        public void parseParameters(List<String> stringParams) {
            learningRateVariants = Arrays.stream(stringParams.get(0).split(";")).map(Double::parseDouble).toList();
            epochs = Arrays.stream(stringParams.get(1).split(";")).map(Double::parseDouble).toList();
            sigmoidParam = List.of(Double.parseDouble(stringParams.get(2)));
            outputErrorParam = List.of(Double.parseDouble(stringParams.get(3)));
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
                case "sigmoidParam" -> {
                    return sigmoidParam;
                }
                case "outputErrorParam" -> {
                    return outputErrorParam;
                }
                default -> {
                    return Collections.emptyList();
                }
            }

        }
    }
}
