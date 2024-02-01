package com.slamperboom.backend.mathematics.analyzers;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

import java.util.ArrayList;
import java.util.List;

public class ARIMA {
    private final int p;
    private final int d;
    private final int q;

    public ARIMA(int p, int d, int q) {
        this.p = p;
        this.d = d;
        this.q = q;
    }

    public List<Double> prediction(List<Double> values){
        if(values.size() < p + d){
            return null;
        }
        //I part
        //находить сначала разницу первого порядка из values, потом разницу из первых порядков
        //затем по получившемуся ряду делаем прогноз и переводим обратно, начиная с первого элемента
        //то есть, необходимо хранить разницы первого и второго порядков
        //количество порядков зависит от 'd', так что имеет смысл завести list таких разниц
        List<List<Double>> listOfSubtractions = new ArrayList<>();
        listOfSubtractions.add(values);
        for(int k=0; k<d; ++k){
            List<Double> list = new ArrayList<>();
            for(int i=0; i<listOfSubtractions.get(k).size()-1; ++i){
                list.add(listOfSubtractions.get(k).get(i+1) - listOfSubtractions.get(k).get(i));
            }
            listOfSubtractions.add(list);
        }
        List<Double> workList = listOfSubtractions.get(listOfSubtractions.size()-1);

        //AR part
        int rowCount = workList.size()-p;
        RealMatrix matrixA = new Array2DRowRealMatrix(rowCount, p);
        RealVector vector = new ArrayRealVector(rowCount);
        for(int i=0; i<rowCount; ++i){
            matrixA.setRow(rowCount-i-1, inverseList(workList.subList(i, i+p)));
            vector.setEntry(i, workList.get(workList.size()-1-i));
        }
        double[] alphas = MatrixUtils.inverse(matrixA.transpose().multiply(matrixA)).multiply(matrixA.transpose()).operate(vector).toArray();

        for(int i=0; i<alphas.length; ++i){
            alphas[i] = Math.signum(alphas[i])*Math.pow(Math.abs(alphas[i]), 1.0/2);
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
        for(int i=0; i<workList.size()-p + 1; ++i){
            RealVector vectorY = new ArrayRealVector(inverseList(resultList.subList(i, i+p)));
            resultList.add(vectorA.dotProduct(vectorY));
        }
        //подготавливаем датасет для нахождения коэффициентов MA
        List<Double> references = resultList.subList(p, resultList.size()-1);
        List<List<Double>> factors = new ArrayList<>();
        for(int i=p; i<resultList.size()-1; ++i){
            references.set(i-p, workList.get(i) - resultList.get(i) - averages.get(i-1));
            factors.add(new ArrayList<>());
            double[] factor = inverseList(epsilons.subList(i-q, i));
            for (double v : factor) {
                factors.get(factors.size() - 1).add(v);
            }
        }
        NeuralNet neuralNet = new NeuralNet(references, factors);
        double[] thetas = neuralNet.linearRegression(0.001, 100000);
        for(int i=p; i<resultList.size(); ++i){
            List<Double> list = new ArrayList<>();
            double[] factor = inverseList(epsilons.subList(i-q, i));
            for(double v:factor){
                list.add(v);
            }
            resultList.set(i, resultList.get(i) + averages.get(i-1) + neuralNet.calcNewValueLinear(list, thetas));
        }

        //разворачиваем разности
        List<List<Double>> reverse = new ArrayList<>();
        reverse.add(resultList);
        for(int k=d-1; k>=0; --k){
            List<Double> nextReverse = new ArrayList<>();
            nextReverse.add(listOfSubtractions.get(k).get(0));
            List<Double> prevReverse = reverse.get(d-1 - k);
            for(int i=0; i<prevReverse.size(); ++i){
                nextReverse.add(nextReverse.get(i) + prevReverse.get(i));
            }
            reverse.add(nextReverse);
        }
        return reverse.get(reverse.size()-1);
    }

    private double[] inverseList(List<Double> values){
        double[] array = new double[values.size()];
        for(int i=0; i<values.size(); ++i){
            array[i] = values.get(values.size()-1-i);
        }
        return array;
    }

    public static double averageMSE(List<Double> original, List<Double> prediction){
        double mse = 0;
        for(int i=0; i<original.size(); ++i){
            mse += Math.pow(original.get(i)-prediction.get(i), 2);
        }
        return mse / original.size();
    }
}
