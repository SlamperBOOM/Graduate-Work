package com.slamperboom.backend.mathematics.analyzers;

import java.util.ArrayList;
import java.util.List;

public class Approximation {
    private final List<Double> references;

    public Approximation(List<Double> references) {
        this.references = references;
    }

    public List<Double> doubleExp(){
        double S = (references.get(0) + references.get(1) + references.get(2))/3;
        double alpha = 0.4;
        double beta = 0.5;
        List<Double> prediction = new ArrayList<>();
        List<Double> betas = new ArrayList<>();
        prediction.add(S);
        double b = (references.get(1) - references.get(0))+
                (references.get(2) - references.get(1))+
                (references.get(3) - references.get(2));
        b /= 3;
        betas.add(b);
        for(int i = 1; i< references.size(); ++i){
            prediction.add(alpha* references.get(i) + (1-alpha)*(prediction.get(i-1) + b));
            b = beta*(prediction.get(i) - prediction.get(i-1)) + (1-beta)*b;
            betas.add(b);
        }
        double nextPred = (alpha* references.get(references.size()-1)
                + (1-alpha)*
                (prediction.get(prediction.size()-1) +
                        (1-beta)*betas.get(betas.size()-1)
                        + beta*(prediction.get(prediction.size()-1) - prediction.get(prediction.size()-2))));
        prediction.add(nextPred);
        return prediction;
    }
}
