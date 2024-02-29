package com.slamperboom.backend.controllers;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxType;
import com.slamperboom.backend.dataLogic.services.IControllerDataService;
import com.slamperboom.backend.dataLogic.services.TaxService;
import com.slamperboom.backend.dataLogic.views.taxes.TaxCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxFactorCreateView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxFactorView;
import com.slamperboom.backend.dataLogic.views.taxes.TaxView;
import com.slamperboom.backend.frontendDTO.TaxValueDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Tax data controller", description = "Manage tax values and other related data")
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
    public List<TaxFactorView> getFactorsNamesForTax(@RequestParam(name = "taxname") String taxName) {
        return taxService.getFactorsNamesForTax(taxName);
    }

    @PostMapping("save")
    public void saveTax(@RequestBody TaxView taxView){
        taxService.saveTaxValue(taxView);
    }

    @PostMapping("tax/factor/add")
    public void addTaxFactorLink(@RequestBody TaxFactorCreateView taxFactorLinkDTO){
        taxService.saveTaxFactorLink(taxFactorLinkDTO);
    }

    @PostMapping("add")
    public void addTaxValues(@RequestParam("type") String type, @RequestBody TaxValueDTO taxValueDTO){
        taxService.addTaxValue(new TaxCreateView(taxValueDTO.taxName(), TaxType.valueOf(type), taxValueDTO.date(), taxValueDTO.value()));
    }

    @PostMapping("add/file")
    public void addTaxValuesViaFile(@RequestParam("file") MultipartFile file,
                                    @RequestParam("taxname") String taxName,
                                    @RequestParam("type") String type){
        if(type.equals(TaxType.TAX.toString())) {
            dataService.parseFileAndAddTaxValues(file, taxName);
        }else{
            dataService.parseFileAndAddFactorValues(file, taxName);
        }
    }

    @DeleteMapping("delete")
    public void deleteTaxValue(@RequestParam("recordid") String id){
        taxService.deleteTaxValue(Long.parseLong(id));
    }

    @DeleteMapping("tax/factor/delete")
    public void deleteTaxFactorLink(@RequestParam("linkid") String id){
        taxService.deleteTaxFactorLink(Long.parseLong(id));
    }
}
