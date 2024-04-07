package com.slamperboom.backend.dataLogic.repositories.prediction;

import com.slamperboom.backend.dataLogic.entities.predictions.Prediction;
import com.slamperboom.backend.dataLogic.entities.predictions.PredictionValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionValuesRepository extends JpaRepository<PredictionValue, Long> {
    @Query("select pv from PredictionValue pv where pv.prediction = :prediction order by pv.date asc")
    List<PredictionValue> findByPrediction(@Param("prediction") Prediction prediction);
}
