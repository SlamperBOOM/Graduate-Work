package com.slamperboom.backend.mathematics;

import com.slamperboom.backend.frontendDTO.AlgorithmDTO;
import com.slamperboom.backend.mathematics.algorithms.PredictionAlgorithm;
import com.slamperboom.backend.mathematics.errors.MathError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ImplementedEntitiesService {
    private final Map<String, PredictionAlgorithm> implementedAlgorithms;
    private final Map<String, MathError> implementedErrors;

    @Autowired
    public ImplementedEntitiesService(List<PredictionAlgorithm> implementedAlgorithms,
                                      List<MathError> implementedErrors) {
        this.implementedAlgorithms = new TreeMap<>(String::compareTo);
        for(PredictionAlgorithm algorithm : implementedAlgorithms){
            this.implementedAlgorithms.put(algorithm.getName(), algorithm);
        }
        this.implementedErrors = new TreeMap<>(String::compareTo);
        for(MathError mathError: implementedErrors){
            this.implementedErrors.put(mathError.getName(), mathError);
        }
    }

    public Collection<MathError> getImplementedErrors(){
        return implementedErrors.values();
    }

    public Collection<PredictionAlgorithm> getImplementedAlgorithms(){
        return implementedAlgorithms.values();
    }

    public Collection<String> getImplementedAlgorithmsNames(){
        return implementedAlgorithms.keySet();
    }

    public MathError getMathErrorByName(String name){
        return implementedErrors.get(name);
    }

    public PredictionAlgorithm getAlgorithmByName(String name){
        return implementedAlgorithms.get(name);
    }

    public List<AlgorithmDTO> getAlgorithmsDescription(){
        return implementedAlgorithms.values()
                .stream().map(a -> new AlgorithmDTO(a.getName(), a.getDescription(), a.getParameters().getParametersNames())).toList();
    }
}
