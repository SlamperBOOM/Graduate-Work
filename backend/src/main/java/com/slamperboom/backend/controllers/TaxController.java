package com.slamperboom.backend.controllers;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxType;
import com.slamperboom.backend.dataLogic.services.IControllerDataService;
import com.slamperboom.backend.dataLogic.services.TaxService;
import com.slamperboom.backend.dataLogic.views.taxes.TaxCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxFactorCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxFactorView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxView;
import com.slamperboom.backend.frontendDTO.TaxValueDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("taxes")
@RequiredArgsConstructor
public class TaxController {
    private final TaxService taxService;
    private final IControllerDataService dataService;

    @GetMapping("values")
    public List<TaxView> getTaxByName(@RequestParam(name = "taxname") String taxName){
        return taxService.getValuesForTax(taxName);
    }

    @GetMapping("tax/names")
    public List<String> getTaxNames(){
        return taxService.getTaxNames();
    }

    @GetMapping("factor/names")
    public List<String> getFactorNames(){
        return taxService.getFactorNames();
    }

    @GetMapping("tax/factors")
    public List<String> getFactorsNamesForTax(@RequestParam(name = "taxname") String taxName){
        return taxService.getFactorsNamesForTax(taxName).stream().map(TaxFactorView::getFactorName).toList();
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

    @PostMapping("tax/factor/add")
    public void addTaxFactorLink(@RequestBody TaxFactorCreateView taxFactorLinkDTO){
        taxService.saveTaxFactorLink(taxFactorLinkDTO);
    }

    @PostMapping("add/tax/file")
    public void addTaxValuesViaFile(@RequestParam("file") MultipartFile file, @RequestParam("taxname") String taxName){
        dataService.parseFileAndAddTaxValues(file, taxName);
    }

    @PostMapping("add/factor/file")
    public void addFactorValuesViaFile(@RequestParam("file") MultipartFile file, @RequestParam("taxname") String taxName){
        dataService.parseFileAndAddFactorValues(file, taxName);
    }
}
