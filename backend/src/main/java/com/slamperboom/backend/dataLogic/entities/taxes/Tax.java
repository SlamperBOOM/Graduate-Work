package com.slamperboom.backend.dataLogic.entities.taxes;

import com.slamperboom.backend.dataLogic.entities.IdEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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

    @Column(name = "date")
    private Date date;

    @Column(name = "value")
    private Double value;
}
