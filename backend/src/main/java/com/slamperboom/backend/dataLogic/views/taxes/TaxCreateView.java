package com.slamperboom.backend.dataLogic.views.taxes;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class TaxCreateView {
    private String taxName;
    private TaxType type;
    private Date date;
    private Double value;
}
