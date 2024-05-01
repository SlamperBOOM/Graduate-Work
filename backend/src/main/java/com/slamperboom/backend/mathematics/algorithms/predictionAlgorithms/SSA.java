package com.slamperboom.backend.mathematics.algorithms.predictionAlgorithms;

import com.slamperboom.backend.mathematics.algorithms.AlgorithmParameters;
import com.slamperboom.backend.mathematics.algorithms.AlgorithmValues;
import com.slamperboom.backend.mathematics.algorithms.PredictionAlgorithm;
import com.slamperboom.backend.mathematics.mathErrors.errors.MRSEError;
import com.slamperboom.backend.mathematics.resultData.ResultParameter;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class SSA implements PredictionAlgorithm {
    private static final String methodName = "SSA";

    private int caterpillarLength;
    private List<Integer> mainComponents;
    private List<Double> singular;

    @Override
    public List<Double> makePrediction(AlgorithmValues referenceValues, AlgorithmParameters parameters) {
        List<Double> values = referenceValues.getReference();
        List<Double> bestPrediction = new LinkedList<>();
        double bestError = Double.MAX_VALUE;
        for(int n = values.size()-2; n >= values.size()/2; --n){
            System.out.println("--------------------------------------------");
            int sigma = values.size() - n + 1;
            RealMatrix matrixZ = new Array2DRowRealMatrix(sigma, n);
            for(int l=0; l<n; ++l){
                RealVector column = new ArrayRealVector(sigma);
                for(int i=0; i<sigma; ++i){
                    column.setEntry(i, values.get(i+l));
                }
                matrixZ.setColumnVector(l, column);
            }
            System.out.format("sigma=%d n=%d%n", sigma, n);
            RealMatrix matrixC = matrixZ.scalarMultiply(1./n).multiply(matrixZ.transpose());
            SingularValueDecomposition svdDecomp = new SingularValueDecomposition(matrixC);
            //выбираем значимые компоненты
            double[] singularValues = svdDecomp.getSingularValues();
            for(int i=0; i<singularValues.length; ++i){
                singularValues[i] = Math.log(singularValues[i]);
                if(Double.isInfinite(singularValues[i])){
                    singularValues[i] = Double.MIN_VALUE * Math.signum(singularValues[i]);
                }
            }
            double mean = new Mean().evaluate(singularValues);
            System.out.println("Mean: " + mean);
            List<Integer> components = new ArrayList<>();
            for(int i=0; i<singularValues.length/2; ++i){
                if(singularValues[i] > mean){
                    components.add(i);
                }
            }
            System.out.println("Singular: " + Arrays.toString(singularValues));
            System.out.println("Components: " + components);
            for(int i=1; i<=components.size(); ++i) {
                var columnsCombIt = CombinatoricsUtils.combinationsIterator(components.size(), i);
                while (columnsCombIt.hasNext()){
                    int[] comb = columnsCombIt.next();
                    List<Integer> combination = new LinkedList<>();
                    for(int val : comb){
                        combination.add(val);
                    }
                    System.out.println("Combination: " + combination);
                    RealMatrix matrixV = svdDecomp.getV();
                    RealMatrix subMatrix = new Array2DRowRealMatrix(matrixV.getRowDimension(), i);
                    for(int k=0; k<combination.size(); ++k){
                        subMatrix.setColumnVector(k, matrixV.getColumnVector(combination.get(k)));
                    }
                    List<Double> prediction = buildPrediction(subMatrix, matrixZ, sigma, values);
                    double error = new MRSEError().calcError(values, prediction);
                    System.out.println("Prediction: " + prediction);
                    System.out.println("Error: " + error);
                    int size = prediction.size();
                    if(error > 1 && error < bestError
                            && Math.abs(prediction.get(size-1) - prediction.get(size-2))
                            < 2*Math.abs(prediction.get(size-2) - prediction.get(size-3))) {
                        bestError = error;
                        bestPrediction = prediction;
                        caterpillarLength = sigma;
                        mainComponents = combination;
                        singular = new LinkedList<>();
                        for(int comp: combination){
                            singular.add(singularValues[comp]);
                        }
                    }
                }
            }
        }
        return bestPrediction;
    }

    private List<Double> buildPrediction(RealMatrix matrixV, RealMatrix matrixZ, int sigma, List<Double> fValues){
        RealMatrix matrixZ1 = matrixV.multiply(matrixV.transpose().multiply(matrixZ));
        List<Double> prediction = new LinkedList<>();
        for(int i=0; i < matrixZ1.getColumnDimension()+matrixZ1.getRowDimension()-1; ++i){
            int x = Math.min(matrixZ1.getRowDimension()-1, i);
            int y = i-x;
            Mean mean = new Mean();
            while(y < matrixZ1.getColumnDimension() && x >= 0){
                mean.increment(matrixZ1.getEntry(x, y));
                x--;
                y++;
            }
            prediction.add(mean.getResult());
        }

        RealMatrix w = matrixV.getRowMatrix(matrixV.getRowDimension()-1);
        RealMatrix V1 = matrixV.getSubMatrix(0, matrixV.getRowDimension()-2, 0, matrixV.getColumnDimension()-1);
        RealMatrix Q = new Array2DRowRealMatrix(sigma - 1, 1);
        for(int i=fValues.size()-sigma+2; i < fValues.size(); ++i){
            Q.setEntry(i-(fValues.size()-sigma+2), 0, fValues.get(i));
        }
        RealMatrix V1inverse = MatrixUtils.inverse(V1.transpose().multiply(V1));
        var result = w.multiply(V1inverse).multiply(V1.transpose()).multiply(Q);
        double predVal = result.getEntry(0, 0);
        prediction.add(predVal);
        return prediction;
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
        List<ResultParameter> resultParameters = new LinkedList<>();
        resultParameters.add(new ResultParameter("Размер рассматриваемого окна", (double) caterpillarLength));
        for(int i=0; i<mainComponents.size(); ++i){
            resultParameters.add(
                    new ResultParameter("Главная компонента " + (i+1), mainComponents.get(i).doubleValue() + 1)
            );
            resultParameters.add(
                    new ResultParameter("Собственное число", singular.get(i))
            );
        }
        return resultParameters;
    }

    private static class SSAParameters implements AlgorithmParameters{
        private static final List<String> paramsNames = List.of();

        @Override
        public List<String> getParametersNames() {
            return paramsNames;
        }

        @Override
        public void parseParameters(List<String> stringParams) {
            AlgorithmParameters.super.parseParameters(stringParams);
        }

        @Override
        public List<Double> getParameterValues(String paramName) {
            return AlgorithmParameters.super.getParameterValues(paramName);
        }
    }
}
