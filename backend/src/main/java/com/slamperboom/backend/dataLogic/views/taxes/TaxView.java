package com.slamperboom.backend.dataLogic.views.taxes;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class TaxView {
    private Long id;
    private String taxName;
    private TaxType type;
    private Date date;
    private Double value;
}
