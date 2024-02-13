package com.slamperboom.backend.frontendDTO;

import java.util.List;

public record AlgorithmDTO(String methodName, String methodDescription, List<String> parameters) {}
