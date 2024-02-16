package com.slamperboom.backend.mathematics;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Scope("singleton")
public class ResultCodeManager {
    private final Map<String, LocalDateTime> codes = new TreeMap<>(String::compareTo);

    public String getNextUid(){
        String code = UUID.randomUUID().toString();
        codes.put(code, LocalDateTime.now());
        return code;
    }

    public boolean checkUid(String uid){
        boolean contains = codes.remove(uid) != null;
        cleanCodes();
        return contains;
    }

    private void cleanCodes(){
        Set<Map.Entry<String, LocalDateTime>> entries = new HashSet<>(codes.entrySet());
        LocalDateTime current = LocalDateTime.now().minusDays(1);
        for(var entry: entries){
            if(entry.getValue().isBefore(current)){
                codes.remove(entry.getKey());
            }
        }
    }
}
