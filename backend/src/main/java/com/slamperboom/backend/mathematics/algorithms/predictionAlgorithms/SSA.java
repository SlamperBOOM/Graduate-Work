package com.slamperboom.backend.mathematics.algorithms.predictionAlgorithms;

import com.slamperboom.backend.mathematics.algorithms.AlgorithmParameters;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.algorithms.PredictionAlgorithm;
import com.slamperboom.backend.mathematics.resultData.ResultParameter;
import org.apache.commons.math3.linear.*;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class SSA implements PredictionAlgorithm {
    private static final String methodName = "SSA";

    @Override
    public List<Double> makePrediction(AlgorithmValues referenceValues, AlgorithmParameters parameters) {
        List<Double> values = referenceValues.getReference();
        List<Double> bestPrediction = new LinkedList<>();
        double bestError = Double.MAX_VALUE;
        for(int n = 1; n < values.size(); ++n){
            int sigma = values.size() - n + 1;
            RealMatrix matrixZ = new Array2DRowRealMatrix(sigma, n);
            for(int l=0; l<n; ++l){
                RealVector column = new ArrayRealVector(sigma);
                for(int i=0; i<sigma; ++i){
                    column.setEntry(i, values.get(i+l));
                }
                matrixZ.setColumnVector(l-1, column);
            }
            RealMatrix matrixC = matrixZ.scalarMultiply(1./n).multiply(matrixZ.transpose());
            SingularValueDecomposition svdDecomp = new SingularValueDecomposition(matrixC);
            matrixC = svdDecomp.getV().multiply(svdDecomp.getS()).multiply(svdDecomp.getVT());
            int mainComponents = 0;
            int eigenValuesCount = svdDecomp.getS().getData().length;
            for(int i=0; i<eigenValuesCount; ++i){
                if(Math.log(svdDecomp.getS().getEntry(i, i)) < 0){
                    break;
                }else{
                    mainComponents = i;
                }
            }
            RealMatrix matrixU = svdDecomp.getV().transpose().multiply(matrixZ);
            matrixU = matrixU.getSubMatrix(0, matrixU.getRowDimension()-1, 0, mainComponents);

            //проанализировать главные компоненты (расположены в столбцах)
            //потом по ним строить прогноз
        }
        return bestPrediction;
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    public AlgorithmParameters getParameters() {
        return new SSAParameters();
    }

    @Override
    public String getDescription() {
        return PredictionAlgorithm.super.getDescription();
    }

    @Override
    public List<ResultParameter> getPredictionParameters() {
        return PredictionAlgorithm.super.getPredictionParameters();
    }

    private static class SSAParameters implements AlgorithmParameters{
        private static final List<String> paramsNames = List.of();

        @Override
        public List<String> getParametersNames() {
            return paramsNames;
        }

        @Override
        public void parseParameters(List<String> stringParams) {

        }

        @Override
        public List<Double> getParameterValues(String paramName) {
            return AlgorithmParameters.super.getParameterValues(paramName);
        }
    }
}
