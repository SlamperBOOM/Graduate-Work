package com.slamperboom.backend.mathematics.errors;

import java.util.List;

public interface MathError {
    double calcError(List<Double> reference, List<Double> predicted);
    String getName();
    boolean compareTo(double o1, double o2);
}
