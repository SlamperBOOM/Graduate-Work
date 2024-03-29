package com.slamperboom.backend.mathematics.algorithms;

import java.util.Collections;
import java.util.List;

public interface AlgorithmParameters {
    default List<String> getParametersNames(){
        return Collections.emptyList();
    }
    default void parseParameters(List<String> stringParams){}
    default List<Double> getParameterValues(String paramName){
        return Collections.emptyList();
    }
}
