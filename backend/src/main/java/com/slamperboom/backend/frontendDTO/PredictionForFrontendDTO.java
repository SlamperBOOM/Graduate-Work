package com.slamperboom.backend.frontendDTO;

import com.slamperboom.backend.mathematics.results.PredictionResultDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PredictionForFrontendDTO {
    private final String resultCode;
    @Setter
    private List<PredictionResultDTO> results;

    public PredictionForFrontendDTO(String resultCode) {
        this.resultCode = resultCode;
    }

    public PredictionForFrontendDTO(String resultCode, PredictionResultDTO resultDTO){
        this.resultCode = resultCode;
        results = List.of(resultDTO);
    }
}
