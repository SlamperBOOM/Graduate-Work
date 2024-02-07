package com.slamperboom.backend.dataLogic.repositories;

import com.slamperboom.backend.dataLogic.entities.taxes.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Long> {
    List<Tax> findByTaxNameOrderByDate(String taxName);

    @Query("select distinct t.taxName from Tax t where t.type = 'TAX'")
    List<String> findTaxNames();
}
