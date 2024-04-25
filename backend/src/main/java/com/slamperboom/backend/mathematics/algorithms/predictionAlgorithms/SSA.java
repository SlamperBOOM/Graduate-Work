package com.slamperboom.backend.mathematics.algorithms.predictionAlgorithms;

import com.slamperboom.backend.mathematics.algorithms.AlgorithmParameters;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.algorithms.PredictionAlgorithm;
import com.slamperboom.backend.mathematics.mathErrors.errors.MRSEError;
import com.slamperboom.backend.mathematics.resultData.ResultParameter;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class SSA implements PredictionAlgorithm {
    private static final String methodName = "SSA";

    private int caterpillarLength;
    private List<Integer> mainComponents;

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
            int mainComponentsCount = svdDecomp.getRank();
            //выбираем значимые компоненты
            RealMatrix matrixU = svdDecomp.getVT().multiply(matrixZ);
            matrixU = matrixU.getSubMatrix(0, matrixU.getRowDimension()-1, 0, mainComponentsCount);

            for(int i=1; i<=mainComponentsCount; ++i) {
                var columnsCombIt = CombinatoricsUtils.combinationsIterator(mainComponentsCount, i);
                while (columnsCombIt.hasNext()){
                    int[] comb = columnsCombIt.next();
                    List<Integer> combination = new LinkedList<>();
                    for(int val : comb){
                        combination.add(val);
                    }
                    RealMatrix subMatrix = new Array2DRowRealMatrix(matrixU.getRowDimension()-1, i);
                    for(Integer column : combination){
                        subMatrix.setColumnVector(column, matrixU.getColumnVector(column));
                    }
                    List<Double> prediction = buildPrediction(subMatrix, matrixZ);
                    double error = new MRSEError().calcError(values, prediction);
                    if(error < bestError){
                        bestError = error;
                        bestPrediction = prediction;
                        caterpillarLength = sigma;
                        mainComponents = combination;
                    }
                }
            }
        }
        return bestPrediction;
    }

    private List<Double> buildPrediction(RealMatrix matrixV, RealMatrix matrixZ){
        RealMatrix matrixZ1 = matrixV.multiply(matrixV.transpose().multiply(matrixZ));
        List<Double> prediction = new LinkedList<>();
        for(int i=0; i < matrixZ1.getColumnDimension()+matrixZ1.getRowDimension()-1; ++i){
            int x = Math.min(matrixZ1.getRowDimension()-1, i);
            for(int k=; i-k<matrixZ1.getColumnDimension(); --k){

            }
        }
        // как-то делать w и V* и по ним делать прогноз
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
        return """
               Алгоритм использует сингулярное разложение для разбиения временного ряда на компоненты,
               и последующий прогноз производится с помощью этих компонент.
               """;
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
