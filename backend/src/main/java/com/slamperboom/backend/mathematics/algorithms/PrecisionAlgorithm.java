package com.slamperboom.backend.mathematics.algorithms;

import java.util.List;

public interface PrecisionAlgorithm {
    List<Double> makePrecision(List<Double> referenceValues, List<Double> params);
    String getName();
}
