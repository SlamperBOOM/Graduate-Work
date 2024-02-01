package com.slamperboom.backend.mathematics;

import com.slamperboom.backend.mathematics.algorithms.PrecisionAlgorithm;
import com.slamperboom.backend.mathematics.errors.MathError;
import com.slamperboom.backend.mathematics.results.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope(value = "singleton")
public class MathService {
    @Autowired
    private List<PrecisionAlgorithm> implementedAlgorithms;
    @Autowired
    private List<MathError> implementedErrors;

    //Что нужно при прогнозе помимо метода и параметров?

    public ResultDTO makePrecision(String methodName, List<Double> params){
        return null;
    }
}
