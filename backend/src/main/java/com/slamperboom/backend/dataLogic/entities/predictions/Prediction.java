package com.slamperboom.backend.dataLogic.entities.predictions;

import com.slamperboom.backend.dataLogic.entities.IdEntity;
import com.slamperboom.backend.dataLogic.entities.taxes.Tax;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "predictions")
@Getter
@Setter
public class Prediction extends IdEntity {
    @JoinColumn(name = "tax")
    @ManyToOne(targetEntity = Tax.class, fetch = FetchType.LAZY)
    private Tax tax;

    @Column(name = "methodName")
    private String methodName;
}
