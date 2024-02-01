package com.slamperboom.backend.mathematics.analyzers;

import java.util.ArrayList;
import java.util.List;

public class NeuralNet {
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
