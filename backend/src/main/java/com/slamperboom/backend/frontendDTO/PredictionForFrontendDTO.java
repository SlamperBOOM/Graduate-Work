package com.slamperboom.backend.frontendDTO;

import java.util.List;

public record PredictionForFrontendDTO(String resultCode, List<ResultDTO> results) {
}