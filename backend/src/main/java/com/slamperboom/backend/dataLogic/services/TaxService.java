package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxFactor;
import com.slamperboom.backend.dataLogic.mappers.TaxMapper;
import com.slamperboom.backend.dataLogic.repositories.TaxFactorRepository;
import com.slamperboom.backend.dataLogic.repositories.TaxRepository;
import com.slamperboom.backend.dataLogic.views.taxes.TaxCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxFactorCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxFactorView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaxService {
    private final TaxRepository taxRepository;
    private final TaxFactorRepository taxFactorRepository;
    private final TaxMapper taxMapper;

    @Transactional(readOnly = true)
    public List<TaxView> getValuesForTax(String taxName){
        return taxRepository.findByTaxNameOrderByDate(taxName).stream().map(taxMapper::fromTaxToView).toList();
    }

    @Transactional(readOnly = true)
    public List<TaxFactorView> getFactorsNamesForTax(String taxName){
        return taxFactorRepository.findByTaxName(taxName).stream().map(taxMapper::fromTaxFactorToView).toList();
    }

    @Transactional(readOnly = true)
    public List<String> getTaxNames(){
        return taxRepository.findTaxNames();
    }

    public List<String> getFactorNames(){
        return taxRepository.findFactorNames();
    }

    @Transactional(readOnly = true)
    public List<List<TaxView>> getFactorsForTax(String taxName){
        List<TaxFactor> factors = taxFactorRepository.findByTaxName(taxName);
        List<List<TaxView>> factorsForTax = new LinkedList<>();
        for(TaxFactor factorLink: factors){
            factorsForTax.add(taxRepository.findByTaxNameOrderByDate(factorLink.getFactorName())
                    .stream().map(taxMapper::fromTaxToView).toList()
            );
        }
        return factorsForTax;
    }
    public void addTaxValue(TaxCreateView createView){
        taxRepository.save(taxMapper.fromCreateToTax(createView));
    }

    public void addTaxValueViaList(List<TaxCreateView> createViews){
        taxRepository.saveAll(createViews.stream().map(taxMapper::fromCreateToTax).toList());
    }

    public void saveTaxValue(TaxView taxView){
        taxRepository.save(taxMapper.fromViewToTax(taxView));
    }

    public void saveTaxFactorLink(TaxFactorCreateView createView){
        taxFactorRepository.save(taxMapper.fromCreateToTaxFactor(createView));
    }
}
