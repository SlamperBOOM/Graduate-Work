package com.slamperboom.backend.dataLogic.entities.predictions;

import com.slamperboom.backend.dataLogic.entities.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "predictions")
@Getter
@Setter
public class Prediction extends IdEntity {
    @Column(name = "taxName")
    private String taxName;

    @Column(name = "methodName")
    private String methodName;

    @Column(name = "date")
    private Date date;

    @Column(name = "value")
    private Double value;
}
