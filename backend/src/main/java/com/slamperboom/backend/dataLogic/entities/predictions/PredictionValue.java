package com.slamperboom.backend.dataLogic.entities.predictions;

import com.slamperboom.backend.dataLogic.entities.IdEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "prediction_values")
@Getter
@Setter
public class PredictionValue extends IdEntity {
    @JoinColumn(name = "prediction")
    @ManyToOne(targetEntity = Prediction.class, fetch = FetchType.LAZY)
    private Prediction prediction;

    @Column(name = "date")
    private Date date;

    @Column(name = "value")
    private Double value;
}
