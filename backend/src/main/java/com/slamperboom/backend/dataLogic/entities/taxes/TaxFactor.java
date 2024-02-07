package com.slamperboom.backend.dataLogic.entities.taxes;

import com.slamperboom.backend.dataLogic.entities.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tax_factor")
@Getter
@Setter
public class TaxFactor extends IdEntity {
    @Column(name = "taxName")
    private String taxName;

    @Column(name = "factorName")
    private String factorName;
}
