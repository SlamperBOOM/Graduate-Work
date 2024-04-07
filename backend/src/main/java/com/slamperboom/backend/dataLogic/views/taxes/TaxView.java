package com.slamperboom.backend.dataLogic.views.taxes;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxType;

public record TaxView(Long id, String taxName, TaxType type) {
}
