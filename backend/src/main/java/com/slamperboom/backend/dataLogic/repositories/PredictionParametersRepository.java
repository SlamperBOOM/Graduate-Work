package com.slamperboom.backend.dataLogic.repositories;

import com.slamperboom.backend.dataLogic.entities.predictions.PredictionParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PredictionParametersRepository extends JpaRepository<PredictionParameter, Long> {
    @Query("select p.parameters from PredictionParameter p where p.taxName = :tax and p.methodName = :method")
    Optional<String> findParamByTaxAndMethod(@Param("tax")String taxName, @Param("method") String methodName);

    @Query("select p from PredictionParameter p where p.taxName = :tax and p.methodName = :method")
    Optional<PredictionParameter> findByTaxAndMethod(@Param("tax")String taxName, @Param("method") String methodName);
}
