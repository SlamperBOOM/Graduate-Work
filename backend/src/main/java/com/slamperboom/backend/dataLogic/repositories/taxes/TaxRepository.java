package com.slamperboom.backend.dataLogic.repositories.taxes;

import com.slamperboom.backend.dataLogic.entities.taxes.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Long> {
    Optional<Tax> findByTaxName(String taxName);

    @Query("select t.taxName from Tax t where t.type = 'TAX'")
    List<String> findTaxNames();

    @Query("select t.taxName from Tax t where t.type = 'FACTOR'")
    List<String> findFactorNames();
}
