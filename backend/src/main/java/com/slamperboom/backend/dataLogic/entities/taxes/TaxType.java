package com.slamperboom.backend.dataLogic.entities.taxes;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TaxType {
    TAX("TAX"),
    FACTOR("FACTOR");

    private final String string;

    @Override
    public String toString() {
        return string;
    }
}
