package com.slamperboom.backend.dataLogic.repositories.taxes;

import com.slamperboom.backend.dataLogic.entities.taxes.Tax;
import com.slamperboom.backend.dataLogic.entities.taxes.TaxFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxFactorRepository extends JpaRepository<TaxFactor, Long> {
    @Query("select tf from TaxFactor tf where tf.tax = :tax")
    List<TaxFactor> findByTax(@Param("tax")Tax tax);
}
