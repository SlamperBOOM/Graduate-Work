package com.slamperboom.backend.dataLogic.repositories.prediction;

import com.slamperboom.backend.dataLogic.entities.predictions.Prediction;
import com.slamperboom.backend.dataLogic.entities.predictions.PredictionParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PredictionParametersRepository extends JpaRepository<PredictionParameter, Long> {
    @Query("select p.parameters from PredictionParameter p where p.prediction = :prediction")
    Optional<String> findParamByPrediction(@Param("prediction")Prediction prediction);

    @Query("select p from PredictionParameter p where p.prediction = :prediction")
    Optional<PredictionParameter> findByPrediction(@Param("prediction") Prediction prediction);
}
