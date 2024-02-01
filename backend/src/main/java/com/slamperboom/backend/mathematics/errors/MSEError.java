package com.slamperboom.backend.mathematics.errors;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = "singleton")
public class MSEError implements MathError{

    @Override
    public double calcError(List<Double> reference, List<Double> actual) {
        return 0;
    }

    @Override
    public String getName() {
        return "MSE";
    }

    @Override
    public double compareTo(double o1, double o2) {
        return Double.compare(o1, o2);
    }
}
