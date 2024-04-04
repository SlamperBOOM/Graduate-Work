package com.slamperboom.backend.controllers;

import com.slamperboom.backend.frontendDTO.PredictionResultDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Scope("singleton")
public class ResultCodeManager {
    private final Map<String, LocalDateTime> codes = new TreeMap<>(String::compareTo);
    private final Map<String, PredictionResultDTO> results = new TreeMap<>(String::compareTo);

    public String getNextUid(PredictionResultDTO resultDTO){
        String code = UUID.randomUUID().toString();
        codes.put(code, LocalDateTime.now());
        results.put(code, resultDTO);
        return code;
    }

    public PredictionResultDTO checkUid(String uid){
        PredictionResultDTO resultDTO = null;
        if(codes.remove(uid) != null){
            resultDTO = results.remove(uid);
        }
        cleanCodes();
        return resultDTO;
    }

    private void cleanCodes(){
        Set<Map.Entry<String, LocalDateTime>> entries = new HashSet<>(codes.entrySet());
        LocalDateTime current = LocalDateTime.now().minusDays(1);
        for(var entry: entries){
            if(entry.getValue().isBefore(current)){
                codes.remove(entry.getKey());
                results.remove(entry.getKey());
            }
        }
    }
}
