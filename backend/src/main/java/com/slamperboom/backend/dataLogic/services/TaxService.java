package com.slamperboom.backend.dataLogic.services;

import com.slamperboom.backend.dataLogic.entities.taxes.Tax;
import com.slamperboom.backend.dataLogic.entities.taxes.TaxFactor;
import com.slamperboom.backend.dataLogic.mappers.TaxMapper;
import com.slamperboom.backend.dataLogic.repositories.taxes.TaxFactorRepository;
import com.slamperboom.backend.dataLogic.repositories.taxes.TaxRepository;
import com.slamperboom.backend.dataLogic.repositories.taxes.TaxValuesRepository;
import com.slamperboom.backend.dataLogic.views.taxes.TaxCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxValueView;
import com.slamperboom.backend.frontendDTO.TaxDTO;
import com.slamperboom.backend.frontendDTO.TaxFactorCreateDTO;
import com.slamperboom.backend.frontendDTO.TaxFactorDTO;
import com.slamperboom.backend.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaxService implements IPredictionTaxService{
    private final TaxRepository taxRepository;
    private final TaxValuesRepository taxValuesRepository;
    private final TaxFactorRepository taxFactorRepository;
    private final TaxMapper taxMapper;

    private Tax getIdByTaxName(String taxName){
        var tax = taxRepository.findByTaxName(taxName);
        return tax.orElse(null);
    }

    @Transactional(readOnly = true)
    public List<TaxDTO> getValuesDTOForTax(String taxName){
        Tax tax = getIdByTaxName(taxName);
        return new LinkedList<>(taxValuesRepository.findByTaxOrderByDate(tax)
                .stream().map(taxMapper::fromTaxAndValueToDTO).toList());
    }

    protected List<TaxValueView> getValuesForTax(String taxName){
        Tax tax = getIdByTaxName(taxName);
        return new LinkedList<>(taxValuesRepository.findByTaxOrderByDate(tax)
                .stream().map(taxMapper::fromValueToView).toList());
    }

    @Transactional(readOnly = true)
    public List<TaxFactorDTO> getFactorsNamesForTax(String taxName){
        Tax tax = getIdByTaxName(taxName);
        return new LinkedList<>(taxFactorRepository.findByTax(tax)
                .stream().map(taxMapper::fromTaxFactorToView).toList());
    }

    @Transactional(readOnly = true)
    public List<String> getTaxNames(){
        return taxRepository.findTaxNames();
    }

    public List<String> getFactorNames(){
        return taxRepository.findFactorNames();
    }

    @Transactional(readOnly = true)
    public List<List<TaxValueView>> getFactorsForTax(String taxName){
        List<List<TaxValueView>> factorsForTax = new LinkedList<>();
        Tax tax = getIdByTaxName(taxName);
        List<TaxFactor> factors = new LinkedList<>(taxFactorRepository.findByTax(tax));
        for(TaxFactor factorLink: factors){
            factorsForTax.add(taxValuesRepository.findByTaxOrderByDate(factorLink.getFactor())
                    .stream().map(taxMapper::fromValueToView).toList()
            );
        }
        return factorsForTax;
    }

    public void addTaxValue(TaxCreateView createView){
        var tax = taxRepository.findByTaxName(createView.taxName());
        if(tax.isEmpty()){
            taxRepository.save(taxMapper.fromCreateToTax(createView));
            taxRepository.flush();
            tax = taxRepository.findByTaxName(createView.taxName());
        }
        taxValuesRepository.save(taxMapper.fromCreateToValue(createView, tax.get()));
    }

    public void addTaxValueViaList(List<TaxCreateView> createViews){
        String taxName = createViews.get(0).taxName();
        var tax = taxRepository.findByTaxName(taxName);
        if(tax.isEmpty()){
            taxRepository.save(taxMapper.fromCreateToTax(createViews.get(0)));
            taxRepository.flush();
            tax = taxRepository.findByTaxName(taxName);
        }
        Optional<Tax> finalTax = tax;
        taxValuesRepository.saveAll(createViews
                .stream().map(taxCreateView -> taxMapper.fromCreateToValue(taxCreateView, finalTax.get())).toList()
        );
    }

    public void saveTaxValue(TaxDTO taxDTO){
        taxValuesRepository.findById(taxDTO.id()).ifPresent(value -> {
            value.setDate(DateUtils.parseDateFromString(taxDTO.date()));
            value.setValue(taxDTO.value());
                });
    }

    public void saveTaxFactorLink(TaxFactorCreateDTO createView){
        var tax = taxRepository.findByTaxName(createView.taxName());
        var factor = taxRepository.findByTaxName(createView.factorName());
        if(tax.isPresent() && factor.isPresent()){
            TaxFactor taxFactor = new TaxFactor();
            taxFactor.setTax(tax.get());
            taxFactor.setFactor(factor.get());
            taxFactorRepository.save(taxFactor);
        }
    }

    public void deleteTaxValue(long id){
        taxValuesRepository.deleteById(id);
    }

    public void deleteTaxFactorLink(long id){
        taxFactorRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Tax getTaxByTaxName(String taxName) {
        var tax = taxRepository.findByTaxName(taxName);
        return tax.orElse(null);
    }
}
