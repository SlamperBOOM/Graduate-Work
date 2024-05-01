package com.slamperboom.backend.dataLogic.repositories.taxes;

import com.slamperboom.backend.dataLogic.entities.taxes.Tax;
import com.slamperboom.backend.dataLogic.entities.taxes.TaxValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxValuesRepository extends JpaRepository<TaxValue, Long> {
    @Query("select v from TaxValue v where v.tax = :tax order by v.date asc")
    List<TaxValue> findByTaxOrderByDate(@Param("tax") Tax tax);

    @Modifying
    @Query("delete from TaxValue v where v.tax = :tax")
    void deleteAllByTax(@Param("tax") Tax tax);
}
