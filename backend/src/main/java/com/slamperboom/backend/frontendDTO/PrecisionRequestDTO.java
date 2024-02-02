package com.slamperboom.backend.frontendDTO;

import java.util.List;

public record PrecisionRequestDTO(String taxName, String methodName, List<String> params) {
}
