package com.slamperboom.backend.frontendDTO;

import java.util.Date;

public record TaxValueDTO(String taxName, Date date, Double value) {
}
