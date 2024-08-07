package com.slamperboom.backend.dataLogic.repositories.prediction;

import com.slamperboom.backend.dataLogic.entities.predictions.Prediction;
import com.slamperboom.backend.dataLogic.entities.predictions.PredictionError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionErrorRepository extends JpaRepository<PredictionError, Long> {
    @Query("select p from PredictionError p where p.prediction = :prediction")
    List<PredictionError> findByPrediction(@Param("prediction") Prediction prediction);
}
