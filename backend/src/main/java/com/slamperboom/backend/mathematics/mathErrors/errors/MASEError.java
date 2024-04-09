package com.slamperboom.backend.mathematics.mathErrors.errors;

import com.slamperboom.backend.mathematics.mathErrors.MathError;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@Scope(value = "singleton")
public class MASEError implements MathError {
    @Override
    public double calcError(List<Double> reference, List<Double> predicted) {
        BigDecimal MAENaive = BigDecimal.ZERO;
        for(int i=1; i<reference.size(); ++i){
            MAENaive = MAENaive.add(BigDecimal.valueOf(Math.abs(reference.get(i) - reference.get(i-1)) / (reference.size()-1)));
        }
        BigDecimal MAE = BigDecimal.ZERO;
        for(int i=0; i<reference.size(); ++i){
            MAE = MAE.add(BigDecimal.valueOf(Math.abs(reference.get(i) - predicted.get(i)) / reference.size()));
        }
        return MAE.divide(MAENaive, MAE.scale(), RoundingMode.FLOOR).doubleValue();
    }

    @Override
    public String getName() {
        return "MASE";
    }

    @Override
    public boolean compareTo(double o1, double o2) {
        return o1 < o2;
    }
}
