package com.slamperboom.backend.dataLogic.entities.predictions;

import com.slamperboom.backend.dataLogic.entities.IdEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "errors")
@Getter
@Setter
@ToString
public class PredictionError extends IdEntity {
    @JoinColumn(name = "prediction")
    @ManyToOne(targetEntity = Prediction.class, fetch = FetchType.LAZY)
    private Prediction prediction;

    @Column(name = "errorName")
    private String errorName;

    @Column(name = "errorValue")
    private Double value;
}
