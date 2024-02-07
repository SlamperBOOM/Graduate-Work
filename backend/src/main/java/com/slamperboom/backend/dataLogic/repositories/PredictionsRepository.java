package com.slamperboom.backend.dataLogic.repositories;

import com.slamperboom.backend.dataLogic.entities.predictions.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionsRepository extends JpaRepository<Prediction, Long> {
    @Query("select p from Prediction p where p.taxName = :tax and p.methodName = :method order by p.date asc")
    List<Prediction> findByTaxAndMethod(@Param("tax")String taxName, @Param("method") String methodName);
}
