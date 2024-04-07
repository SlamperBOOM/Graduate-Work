package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.dataLogic.entities.taxes.Tax;

public interface IPredictionTaxService {
    Tax getTaxByTaxName(String taxName);
}
