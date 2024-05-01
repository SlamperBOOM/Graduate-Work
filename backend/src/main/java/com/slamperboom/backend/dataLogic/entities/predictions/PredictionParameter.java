package com.slamperboom.backend.dataLogic.entities.predictions;

import com.slamperboom.backend.dataLogic.entities.IdEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "parameters")
@Getter
@Setter
public class PredictionParameter extends IdEntity {
    @JoinColumn(name = "prediction")
    @ManyToOne(targetEntity = Prediction.class, fetch = FetchType.LAZY)
    private Prediction prediction;

    @Column(name = "parameter_name")
    private String parameterName;

    @Column(name = "value")
    private Double value;
}
