package com.slamperboom.backend.dataLogic.entities.taxes;

import com.slamperboom.backend.dataLogic.entities.IdEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tax_factor")
@Getter
@Setter
public class TaxFactor extends IdEntity {
    @JoinColumn(name = "tax")
    @ManyToOne(targetEntity = Tax.class, fetch = FetchType.LAZY)
    private Tax tax;

    @JoinColumn(name = "factor")
    @ManyToOne(targetEntity = Tax.class, fetch = FetchType.LAZY)
    private Tax factor;
}
