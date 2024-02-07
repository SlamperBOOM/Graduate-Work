package com.slamperboom.backend.frontendDTO;

import java.util.List;

public record PredictionRequestDTO(String taxName, String methodName, List<String> params) {
}
