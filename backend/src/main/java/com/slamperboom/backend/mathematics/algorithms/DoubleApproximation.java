package com.slamperboom.backend.mathematics.algorithms;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DoubleApproximation implements PrecisionAlgorithm{
    private static final String methodName = "Double Approximation";

    @Override
    public List<Double> makePrecision(AlgorithmValues referenceValues, AlgorithmParameters parameters) {
        List<Double> reference = referenceValues.getReference();
        if(reference.size() < 3){
            throw new IllegalArgumentException();
        }
        double S = (reference.get(0) + reference.get(1) + reference.get(2))/3;
        double alpha = 0.4;
        double beta = 0.5;
        List<Double> prediction = new ArrayList<>();
        List<Double> betas = new ArrayList<>();
        prediction.add(S);
        double b = (reference.get(1) - reference.get(0))+
                (reference.get(2) - reference.get(1))+
                (reference.get(3) - reference.get(2));
        b /= 3;
        betas.add(b);
        for(int i = 1; i< reference.size(); ++i){
            prediction.add(alpha* reference.get(i) + (1-alpha)*(prediction.get(i-1) + b));
            b = beta*(prediction.get(i) - prediction.get(i-1)) + (1-beta)*b;
            betas.add(b);
        }
        double nextPred = (alpha* reference.get(reference.size()-1)
                + (1-alpha)*
                (prediction.get(prediction.size()-1) +
                        (1-beta)*betas.get(betas.size()-1)
                        + beta*(prediction.get(prediction.size()-1) - prediction.get(prediction.size()-2))));
        prediction.add(nextPred);
        return prediction;
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    public AlgorithmParameters getParameters() {
        return new DoubleApproximationParameters();
    }

    @Override
    public String getDescription() {
        return """
                
                """;
    }

    private static class DoubleApproximationParameters implements AlgorithmParameters{}
}
