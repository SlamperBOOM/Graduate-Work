package com.slamperboom.backend.mathematics.errors;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Scope(value = "singleton")
public class MSEError implements MathError{

    @Override
    public double calcError(List<Double> reference, List<Double> predicted) {
        BigDecimal sum = BigDecimal.ZERO;
        for(int i=0; i<reference.size(); ++i){
            sum = sum.add(BigDecimal.valueOf(Math.pow(reference.get(i) - predicted.get(i), 2)/reference.size()));
        }
        return sum.compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) < 0 ? sum.doubleValue() : Double.MAX_VALUE;
    }

    @Override
    public String getName() {
        return "MSE";
    }

    @Override
    public boolean compareTo(double o1, double o2) {
        return o1 < o2;
    }
}
