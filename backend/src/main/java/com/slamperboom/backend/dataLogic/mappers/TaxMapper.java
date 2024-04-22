package com.slamperboom.backend.dataLogic.mappers;

import com.slamperboom.backend.dataLogic.entities.taxes.Tax;
import com.slamperboom.backend.dataLogic.entities.taxes.TaxFactor;
import com.slamperboom.backend.dataLogic.entities.taxes.TaxValue;
import com.slamperboom.backend.dataLogic.views.taxes.TaxCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxValueView;
import com.slamperboom.backend.frontendDTO.TaxDTO;
import com.slamperboom.backend.frontendDTO.TaxFactorDTO;
import com.slamperboom.backend.utils.DateUtils;
import org.springframework.stereotype.Component;

@Component
public class TaxMapper {
    public TaxDTO fromTaxAndValueToDTO(TaxValue value){
        return new TaxDTO(value.getId(),
                value.getTax().getTaxName(),
                value.getTax().getType(),
                DateUtils.formatDateToString(value.getDate()),
                value.getValue());
    }

    public TaxValueView fromValueToView(TaxValue value){
        return new TaxValueView(value.getDate(), value.getValue());
    }

    public Tax fromCreateToTax(TaxCreateView view){
        Tax tax = new Tax();
        tax.setTaxName(view.taxName());
        tax.setType(view.type());
        return tax;
    }

    public TaxValue fromCreateToValue(TaxCreateView view, Tax tax){
        TaxValue value = new TaxValue();
        value.setTax(tax);
        value.setValue(view.value());
        value.setDate(view.date());
        return value;
    }

    public TaxFactorDTO fromTaxFactorToView(TaxFactor taxFactor){
        return new TaxFactorDTO(taxFactor.getId(), taxFactor.getTax().getTaxName(), taxFactor.getFactor().getTaxName());
    }
}
