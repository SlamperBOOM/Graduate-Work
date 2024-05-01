package com.slamperboom.backend.mathematics.algorithms.predictionAlgorithms;

import com.slamperboom.backend.mathematics.algorithms.AlgorithmParameters;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.algorithms.PredictionAlgorithm;
import com.slamperboom.backend.mathematics.mathErrors.errors.MRSEError;
import com.slamperboom.backend.mathematics.mathErrors.MathError;
import com.slamperboom.backend.mathematics.resultData.ResultParameter;
import org.apache.commons.math3.linear.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class ARIMA implements PredictionAlgorithm {
    private static final String methodName = "ARIMA";
    private static final int maxP = 5;
    private static final int maxQ = 5;
    private static final int maxD = 3;
    private int bestP;
    private int bestD;
    private int bestQ;
    private int currentD;

    @Override
    public List<Double> makePrediction(AlgorithmValues referenceValues, AlgorithmParameters parameters) {
        List<Double> values = referenceValues.getReference();
        double bestMSE = Double.MAX_VALUE;
        List<Double> bestPredict = new LinkedList<>();
        MathError mse = new MRSEError();
        for(int p = 0; p <= maxP; ++p){
            for(int q = 0; q <= maxQ; ++q){
                //Сделать различные проверки, чтобы не прогонять невозможные варианты
                try {
                    List<Double> prediction = makePrediction(values, p, q);
                    double currentError = mse.calcError(values, prediction);
                    if (mse.compareTo(currentError, bestMSE)) {
                        bestMSE = currentError;
                        bestPredict = prediction;
                        bestP = p;
                        bestQ = q;
                        bestD = currentD;
                    }
                }catch (Exception ignored){}
            }
        }
        return bestPredict;
    }

    private List<Double> makePrediction(List<Double> values, int p, int q){

        //Делаем разницы по необходимости
        List<List<Double>> timeSeries = integrationPart(values, p);
        List<Double> workList = timeSeries.get(timeSeries.size()-1);
        System.out.format("P: %d, D: %d, Q: %d%n", p, timeSeries.size()-1, q);
        //Вычислять коэффициенты для модели AR и считать AR часть

        List<Double> arPart = arPart(workList, p);
        System.out.println("AR part: " + arPart);
        //Вычислять MA часть
        List<Double> maPart = maPart(workList, q);
        System.out.println("MA part: " + maPart);

        //Сложить все и вернуть к изначальному варианту
        return getResult(arPart, maPart, timeSeries);
    }

    private List<List<Double>> integrationPart(List<Double> values, int p){
        List<List<Double>> timeSeries = new ArrayList<>();
        timeSeries.add(values);
        currentD = 0;
        while(!isStationery(timeSeries.get(timeSeries.size()-1), p) && currentD < maxD){
            List<Double> currentList = timeSeries.get(timeSeries.size()-1);
            List<Double> newList = new ArrayList<>();
            for(int i=0; i<currentList.size()-1; ++i){
                newList.add(currentList.get(i+1) - currentList.get(i));
            }
            timeSeries.add(newList);
            currentD++;
        }
        return timeSeries;
    }

    private boolean isStationery(List<Double> values, int k){
        //вычислять стационарность ряда через ACF
        double mean = mean(values);
        List<Double> acfValues = new ArrayList<>();
        for(int i=0; i<values.size(); ++i){
            double sum = 0;
            for(int p=0; p<values.size()-i; ++p){
                sum += (values.get(p)-mean)*(values.get(p+i)-mean);
            }
            sum /= values.size();
            acfValues.add(sum);
        }
        double s0 = acfValues.get(0);
        acfValues = acfValues.stream().map(aDouble -> aDouble/s0).toList();
        //нужно проанализировать acfValues и понять, является ряд стационарным или нет
        for(int i=k+1; i<acfValues.size(); ++i){
            if(acfValues.get(i) > 0.25){
                return false;
            }
        }
        return true;
    }

    private double mean(List<Double> values){
        if(values.isEmpty()){
            return .0;
        }
        double mean = 0;
        for(double val: values){
            mean += val;
        }
        return mean/values.size();
    }

    private List<Double> arPart(List<Double> workList, int p){
        if(p == 0){
            List<Double> result = new ArrayList<>(workList);
            result.add((result.get(result.size()-1) + result.get(result.size()-2))/2);
            return result;
        }
        double[] coeffs;
        RealMatrix matrixX = getMatrixXForAR(workList, p);
        RealVector vectorY = new ArrayRealVector();
        for (int i = p; i < workList.size(); ++i) {
            vectorY = vectorY.append(workList.get(i));
        }
        coeffs = MatrixUtils.inverse(matrixX.transpose().multiply(matrixX))
                .multiply(matrixX.transpose()).operate(vectorY).toArray();
        System.out.println("AR coeffs: " + Arrays.toString(coeffs));

        List<Double> copyOfWorkList = new ArrayList<>(workList);
        for(int i=1; i<=p; ++i){
            copyOfWorkList.add(0, calcPrevSeriesValue(coeffs, i));
        }
        System.out.println("All workList: " + copyOfWorkList);
        List<Double> arPart = new ArrayList<>();
        for(int i=p-1; i<copyOfWorkList.size(); ++i){
            double val = coeffs[0];
            for(int k=0; k<p; ++k){
                val += coeffs[k+1] * copyOfWorkList.get(i-k);
            }
            arPart.add(val);
        }
        return arPart;
    }

    private double calcPrevSeriesValue(double[] coeffs, int p){
        double sum = 0;
        for(int i=1; i<=p; ++i){
            sum += coeffs[i];
        }
        return coeffs[0]/(1 - sum);
    }

    private RealMatrix getMatrixXForAR(List<Double> workList, int p) {
        RealMatrix matrixX = new Array2DRowRealMatrix(workList.size()-p, p+1);
        RealVector zeroVector = new ArrayRealVector(workList.size()-p);
        zeroVector.set(1);
        matrixX.setColumnVector(0, zeroVector);
        for(int i = p; i < workList.size(); ++i){
            for(int j = 1; j < p +1; ++j){
                matrixX.setEntry(i-p, j, workList.get(i-j+1));
            }
        }
        return matrixX;
    }

    private List<Double> maPart(List<Double> workList, int q){
        if(q == 0){
            return simpleMA(workList);
        }

        List<Double> maPart = new ArrayList<>();
        q = Math.min(q, workList.size());
        List<Double> workingSubList = new LinkedList<>(workList.subList(0, q));
        double mean = mean(workingSubList);

        List<Double> errors = new ArrayList<>();
        double[] coeffs;
        if(q == 1){
            errors.add(workList.get(0) - mean);
            coeffs = new double[]{0.5};
        }else{
            RealMatrix matrixA = new Array2DRowRealMatrix(q, q);
            for(int i=0; i<q; ++i){
                errors.add(workList.get(i) - mean);
                RealVector equation = new ArrayRealVector(q);
                equation.set(0);
                for(int k=0; k<i+1; ++k){
                    equation.setEntry(k, errors.get(k));
                }
                matrixA.setRowVector(i, equation);
            }
            RealVector vectorY = new ArrayRealVector(q);
            for(int i=0; i<q; ++i){
                vectorY.setEntry(i, workList.get(i));
            }
            DecompositionSolver solver = new QRDecomposition(matrixA).getSolver();
            coeffs = solver.solve(vectorY).toArray();
        }


        for(int i=0; i<q; ++i){
            double sum = 0;
            for(int k=0; k<i+1; ++k){
                sum += coeffs[k]*errors.get(k);
            }
            maPart.add(sum+mean);
        }
        for(int i=q; i<workList.size(); ++i){
            workingSubList.remove(0);
            workingSubList.add(workList.get(i));
            mean = mean(workingSubList);
            errors.clear();
            for(double val: workingSubList){
                errors.add(val - mean);
            }
            double sum = 0;
            for(int k=0; k<q; ++k){
                sum += coeffs[k]*errors.get(k);
            }
            maPart.add(sum+mean);
        }
        maPart.add((maPart.get(maPart.size()-1) * 3 - maPart.get(maPart.size()-2))/2);
        return maPart;
    }

    private List<Double> simpleMA(List<Double> workList){
        double paramS = 2;
        List<Double> result = new ArrayList<>();
        result.add(workList.get(0));
        for(int i=1; i<workList.size(); ++i){
            double today = workList.get(i) * (paramS / (1 + workList.size()));
            double yesterday = result.get(i-1) * (1 - (paramS / (1 + workList.size())));
            result.add(today+yesterday);
        }
        result.add((result.get(result.size()-1) * 3 - result.get(result.size()-2))/2);
        return result;
    }

    private List<Double> getResult(List<Double> arPart, List<Double> maPart, List<List<Double>> timeSeries) {
        List<Double> result = new ArrayList<>(arPart);
        for(int i=0; i<result.size(); ++i){
            result.set(i, result.get(i) + maPart.get(i));
        }
        if(timeSeries.size() == 1){
            return result;
        }
        for(int i = timeSeries.size()-1; i>=0; --i){
            List<Double> newList = new ArrayList<>();
            newList.add(timeSeries.get(i).get(0));
            for(int k=0; k < result.size(); ++k){
                newList.add(newList.get(k) + result.get(k));
            }
            result = newList;
        }
        return result;
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
                1. AR - авторегрессия. Порядок авторегрессии регулируется параметром p, который будет показан в результате прогноза;
                2. I - интегрирующая. Порядок интегрирования регулируется параметром d;
                3. MA - скользящее среднее. Порядок скользящего среднего регулируется параметром q;
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

    private static class ARIMAParameters implements AlgorithmParameters{}
}
