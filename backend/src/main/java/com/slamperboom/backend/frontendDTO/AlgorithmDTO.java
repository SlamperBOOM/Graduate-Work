package com.slamperboom.backend.frontendDTO;

import java.util.List;

public record AlgorithmDTO(String algorithmName, String algorithmDescription, List<String> parameters) {}
