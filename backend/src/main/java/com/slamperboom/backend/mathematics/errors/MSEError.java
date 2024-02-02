package com.slamperboom.backend.mathematics.errors;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = "singleton")
public class MSEError implements MathError{

    @Override
    public double calcError(List<Double> reference, List<Double> predicted) {
        double sum = 0;
        for(int i=0; i<reference.size(); ++i){
            sum+= Math.pow(reference.get(i) - predicted.get(i), 2)/reference.size();
        }
        return sum;
    }

    @Override
    public String getName() {
        return "MSE";
    }

    @Override
    public double compareTo(double o1, double o2) {
        return o1 < o2 ? 1 : -1;
    }
}
