package com.slamperboom.backend.mathematics.errors;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@Scope(value = "singleton")
public class ADTError implements MathError{
    @Override
    public double calcError(List<Double> reference, List<Double> predicted) {
        int dimension = reference.size();
        RealMatrix matrix = new Array2DRowRealMatrix(dimension, dimension);
        for(int i = 0; i< dimension; ++i){
            for(int j = 0; j< dimension; ++j){
                matrix.setEntry(i, j, Math.abs(reference.get(i) - predicted.get(j)));
            }
        }
        RealMatrix transformMatrix = new Array2DRowRealMatrix(dimension, dimension);
        transformMatrix.setEntry(0, 0, matrix.getEntry(0, 0));
        for(int i=0; i< dimension; ++i){
            for(int j=0; j< dimension; ++j){
                if(i-1 >= 0 && j-1 >= 0){
                    transformMatrix.setEntry(i, j, matrix.getEntry(i, j) +
                            Math.min(
                                    Math.min(transformMatrix.getEntry(i-1, j-1), transformMatrix.getEntry(i-1, j)),
                                    transformMatrix.getEntry(i, j-1)
                            ));
                }else if(i-1 >= 0){
                    transformMatrix.setEntry(i, j, matrix.getEntry(i, j) +
                            transformMatrix.getEntry(i-1, j));
                }else {
                    transformMatrix.setEntry(i, j, matrix.getEntry(i, j) +
                            transformMatrix.getEntry(i, j-1));
                }
            }
        }
        List<Double> path = new LinkedList<>();
        path.add(transformMatrix.getEntry(dimension-1, dimension-1));
        int i = dimension-1, j = dimension-1;
        while(i > 0 || j > 0){
            if(i-1 >= 0 && j-1 >= 0){
                double min = Math.min(
                        Math.min(transformMatrix.getEntry(i-1, j-1), transformMatrix.getEntry(i-1, j)),
                        transformMatrix.getEntry(i, j-1)
                );
                if(transformMatrix.getEntry(i-1, j) == min){
                    i--;
                } else if (transformMatrix.getEntry(i-1, j-1) == min) {
                    i--;
                    j--;
                }else{
                    j--;
                }
                path.add(min);
            }else if(i-1 >= 0){
                path.add(transformMatrix.getEntry(i-1, j));
                i--;
            }else {
                path.add(transformMatrix.getEntry(i, j-1));
                j--;
            }
        }
        double adt = 0;
        for(double val : path){
            adt += val;
        }
        return adt / path.size();
    }

    @Override
    public String getName() {
        return "ADT";
    }

    @Override
    public boolean compareTo(double o1, double o2) {
        return o1 < o2;
    }
}
