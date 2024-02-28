package com.slamperboom.backend.dataLogic.mappers;

import com.slamperboom.backend.dataLogic.entities.taxes.Tax;
import com.slamperboom.backend.dataLogic.entities.taxes.TaxFactor;
import com.slamperboom.backend.dataLogic.views.taxes.TaxCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxFactorCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxFactorView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxView;
import org.springframework.stereotype.Component;

@Component
public class TaxMapper {
    public TaxView fromTaxToView(Tax tax){
        return new TaxView(tax.getId(), tax.getTaxName(), tax.getType(), tax.getDate(), tax.getValue());
    }

    public Tax fromViewToTax(TaxView view){
        Tax tax = new Tax();
        tax.setId(view.id());
        tax.setTaxName(view.taxName());
        tax.setType(view.type());
        tax.setDate(view.date());
        tax.setValue(view.value());
        return tax;
    }

    public Tax fromCreateToTax(TaxCreateView view){
        Tax tax = new Tax();
        tax.setTaxName(view.taxName());
        tax.setType(view.type());
        tax.setDate(view.date());
        tax.setValue(view.value());
        return tax;
    }

    public TaxFactorView fromTaxFactorToView(TaxFactor taxFactor){
        return new TaxFactorView(taxFactor.getId(), taxFactor.getTaxName(), taxFactor.getFactorName());
    }

    public TaxFactor fromCreateToTaxFactor(TaxFactorCreateView createView){
        TaxFactor taxFactor = new TaxFactor();
        taxFactor.setTaxName(createView.taxName());
        taxFactor.setFactorName(createView.factorName());
        return taxFactor;
    }
}
