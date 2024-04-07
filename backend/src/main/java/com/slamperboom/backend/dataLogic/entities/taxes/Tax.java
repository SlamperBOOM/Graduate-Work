package com.slamperboom.backend.dataLogic.entities.taxes;

import com.slamperboom.backend.dataLogic.entities.IdEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "taxes")
@Getter
@Setter
public class Tax extends IdEntity {
    @Column(name = "taxName")
    private String taxName;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TaxType type;
}
