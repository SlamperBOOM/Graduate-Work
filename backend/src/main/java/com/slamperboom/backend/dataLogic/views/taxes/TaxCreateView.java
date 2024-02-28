package com.slamperboom.backend.dataLogic.views.taxes;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxType;

import java.util.Date;

public record TaxCreateView(String taxName, TaxType type, Date date, Double value) {
}
