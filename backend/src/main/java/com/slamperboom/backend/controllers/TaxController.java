package com.slamperboom.backend.controllers;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxType;
import com.slamperboom.backend.dataLogic.services.TaxService;
import com.slamperboom.backend.dataLogic.views.taxes.TaxCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxFactorCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxFactorView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxView;
import com.slamperboom.backend.frontendDTO.TaxValueDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("taxes")
@RequiredArgsConstructor
public class TaxController {
    private final TaxService taxService;

    @GetMapping("values")
    public List<TaxView> getTaxByName(@RequestParam(name = "taxname") String taxName){
        return taxService.getValuesForTax(taxName);
    }

    @GetMapping("names")
    public List<String> getTaxNames(){
        return taxService.getTaxNames();
    }

    @GetMapping("tax/factors")
    public List<TaxFactorView> getFactorsNamesForTax(@RequestParam(name = "taxname") String taxName){
        return taxService.getFactorsNamesForTax(taxName);
    }

    @PostMapping("add/tax")
    public void addTaxValues(@RequestBody TaxValueDTO taxValueDTO){
        taxService.addTaxValue(new TaxCreateView(taxValueDTO.taxName(), TaxType.TAX, taxValueDTO.date(), taxValueDTO.value()));
    }

    @PostMapping("add/factor")
    public void addFactorValues(@RequestBody TaxValueDTO taxValueDTO){
        taxService.addTaxValue(new TaxCreateView(taxValueDTO.taxName(), TaxType.FACTOR, taxValueDTO.date(), taxValueDTO.value()));
    }

    @PostMapping("save")
    public void saveTax(@RequestBody TaxView taxView){
        taxService.saveTaxValue(taxView);
    }

    @PostMapping("tax/factors/add")
    public void addTaxFactorLink(@RequestBody TaxFactorCreateView taxFactorLinkDTO){
        taxService.saveTaxFactorLink(taxFactorLinkDTO);
    }

    //добавить функцию добавления из файла
}
