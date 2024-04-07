package com.slamperboom.backend.dataLogic.repositories.prediction;

import com.slamperboom.backend.dataLogic.entities.predictions.Prediction;
import com.slamperboom.backend.dataLogic.entities.taxes.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PredictionsRepository extends JpaRepository<Prediction, Long> {
    @Query("select p from Prediction p where p.tax = :tax and p.methodName = :method")
    Optional<Prediction> findByTaxAndMethod(@Param("tax") Tax tax, @Param("method") String methodName);
}
