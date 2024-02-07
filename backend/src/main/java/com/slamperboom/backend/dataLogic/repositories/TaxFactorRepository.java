package com.slamperboom.backend.dataLogic.repositories;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxFactorRepository extends JpaRepository<TaxFactor, Long> {
    List<TaxFactor> findByTaxName(String taxName);
}
