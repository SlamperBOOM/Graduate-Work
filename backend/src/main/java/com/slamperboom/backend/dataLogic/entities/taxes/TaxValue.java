package com.slamperboom.backend.dataLogic.entities.taxes;

import com.slamperboom.backend.dataLogic.entities.IdEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "tax_values")
@Getter
@Setter
public class TaxValue extends IdEntity {
    @JoinColumn(name = "tax")
    @ManyToOne(targetEntity = Tax.class, fetch = FetchType.LAZY)
    private Tax tax;

    @Column(name = "date")
    private Date date;

    @Column(name = "value")
    private Double value;
}
