package com.slamperboom.backend.mathematics.algorithms.predictionAlgorithms;

import com.slamperboom.backend.mathematics.algorithms.AlgorithmParameters;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.algorithms.PredictionAlgorithm;
import com.slamperboom.backend.mathematics.errors.MRSEError;
import com.slamperboom.backend.mathematics.resultsDTO.ResultParameterDTO;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
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
        List<Integer> pParams = parameters.getParameter("p").stream().map(Double::intValue).toList();
        List<Integer> dParams = parameters.getParameter("d").stream().map(Double::intValue).toList();
        int qParam = parameters.getParameter("q").get(0).intValue();
        List<Double> values = referenceValues.getReference();
        double bestMSE = Double.MAX_VALUE;
        List<Double> bestPredict = new LinkedList<>();
        for(int p : pParams) {
            for(int d: dParams){
                try {
                    if (values.size() <= p + d) {
                        continue;
                    }
                    int q = Math.min(qParam, p);
                    //I part
                    //находить сначала разницу первого порядка из values, потом разницу из первых порядков
                    //затем по получившемуся ряду делаем прогноз и переводим обратно, начиная с первого элемента
                    //то есть, необходимо хранить разницы первого и второго порядков
                    //количество порядков зависит от 'd', так что имеет смысл завести list таких разниц
                    List<List<Double>> listOfSubtractions = new ArrayList<>();
                    listOfSubtractions.add(new ArrayList<>(values));
                    for (int k = 0; k < d; ++k) {
                        List<Double> list = new ArrayList<>();
                        for (int i = 0; i < listOfSubtractions.get(k).size() - 1; ++i) {
                            list.add(listOfSubtractions.get(k).get(i + 1) - listOfSubtractions.get(k).get(i));
                        }
                        listOfSubtractions.add(list);
                    }
                    List<Double> workList = listOfSubtractions.get(listOfSubtractions.size() - 1);

                    //AR part
                    int rowCount = workList.size() - p;
                    RealMatrix matrixA = new Array2DRowRealMatrix(rowCount, p);
                    RealVector vector = new ArrayRealVector(rowCount);
                    for (int i = 0; i < rowCount; ++i) {
                        matrixA.setRow(rowCount - i - 1, inverseList(workList.subList(i, i + p)));
                        vector.setEntry(i, workList.get(workList.size() - 1 - i));
                    }
                    double[] alphas = MatrixUtils.inverse(matrixA.transpose().multiply(matrixA)).multiply(matrixA.transpose()).operate(vector).toArray();

                    for (int i = 0; i < alphas.length; ++i) {
                        alphas[i] = Math.signum(alphas[i]) * Math.pow(Math.abs(alphas[i]), 1.0 / 2);
                    }

                    //MA part
                    //epsilon - отклонение значения от среднего
                    //mu - среднее ряда до t
                    List<Double> averages = new ArrayList<>();
                    List<Double> epsilons = new ArrayList<>();
                    Mean mean = new Mean();
                    for (Double value : workList) {
                        mean.increment(value);
                        averages.add(mean.getResult());
                        epsilons.add(mean.getResult() - value);
                    }

                    //делаем прогноз следующего workList
                    List<Double> resultList = new ArrayList<>(workList.subList(0, p));
                    RealVector vectorA = new ArrayRealVector(alphas);
                    for (int i = 0; i < workList.size() - p + 1; ++i) {
                        RealVector vectorY = new ArrayRealVector(inverseList(resultList.subList(i, i + p)));
                        resultList.add(vectorA.dotProduct(vectorY));
                    }
                    //подготавливаем датасет для нахождения коэффициентов MA
                    List<Double> references = resultList.subList(p, resultList.size() - 1);
                    List<List<Double>> factors = new ArrayList<>();
                    for (int i = p; i < resultList.size() - 1; ++i) {
                        references.set(i - p, workList.get(i) - resultList.get(i) - averages.get(i - 1));
                        factors.add(new ArrayList<>());
                        double[] factor = inverseList(epsilons.subList(Math.max(i - q, 0), i));
                        for (double v : factor) {
                            factors.get(factors.size() - 1).add(v);
                        }
                    }
                    NeuralNet neuralNet = new NeuralNet(references, factors);
                    double[] thetas = neuralNet.linearRegression(0.001, 100000);
                    for (int i = p; i < resultList.size(); ++i) {
                        List<Double> list = new ArrayList<>();
                        double[] factor = inverseList(epsilons.subList(Math.max(i - q, 0), i));
                        for (double v : factor) {
                            list.add(v);
                        }
                        resultList.set(i, resultList.get(i) + averages.get(i - 1) + neuralNet.calcNewValueLinear(list, thetas));
                    }

                    //разворачиваем разности
                    List<List<Double>> reverse = new ArrayList<>();
                    reverse.add(resultList);
                    for (int k = d - 1; k >= 0; --k) {
                        List<Double> nextReverse = new ArrayList<>();
                        nextReverse.add(listOfSubtractions.get(k).get(0));
                        List<Double> prevReverse = reverse.get(d - 1 - k);
                        for (int i = 0; i < prevReverse.size(); ++i) {
                            nextReverse.add(nextReverse.get(i) + prevReverse.get(i));
                        }
                        reverse.add(nextReverse);
                    }

                    double error = new MRSEError().calcError(references, reverse.get(reverse.size() - 1));
                    if (error < bestMSE) {
                        bestP = p;
                        bestD = d;
                        bestQ = q;
                        bestPredict = reverse.get(reverse.size() - 1);
                        bestMSE = error;
                    }
                }catch (SingularMatrixException e){
                }
            }
        }

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
    public List<ResultParameterDTO> getPredictionParameters() {
        List<ResultParameterDTO> parameters = new LinkedList<>();
        parameters.add(new ResultParameterDTO("P", (double) bestP));
        parameters.add(new ResultParameterDTO("D", (double) bestD));
        parameters.add(new ResultParameterDTO("Q", (double) bestQ));
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
            paramP = Arrays.stream(stringParams.get(0).split(";")).map(Double::parseDouble).sorted().toList();
            paramD = Arrays.stream(stringParams.get(1).split(";")).map(Double::parseDouble).sorted().toList();
            paramQ = List.of(Double.parseDouble(stringParams.get(2)));
        }

        @Override
        public List<Double> getParameter(String paramName) {
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
