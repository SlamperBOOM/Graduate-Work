package com.slamperboom.backend.mathematics.mathErrors;

import java.util.List;

public interface MathError {
    double calcError(List<Double> reference, List<Double> predicted);
    String getName();

    /**
     * @return возвращает true если o1 лучше o2 в контексте данного вида ошибки
     */
    boolean compareTo(double o1, double o2);
}
