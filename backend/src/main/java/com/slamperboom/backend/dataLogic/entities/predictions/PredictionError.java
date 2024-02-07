package com.slamperboom.backend.dataLogic.entities.predictions;

import com.slamperboom.backend.dataLogic.entities.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "errors")
@Getter
@Setter
@ToString
public class PredictionError extends IdEntity {
    @Column(name = "taxName")
    private String taxName;

    @Column(name = "methodName")
    private String methodName;

    @Column(name = "errorName")
    private String errorName;

    @Column(name = "errorValue")
    private Double value;
}
