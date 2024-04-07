package com.slamperboom.backend.controllers;

import com.slamperboom.backend.dataLogic.entities.taxes.TaxType;
import com.slamperboom.backend.dataLogic.services.IControllerDataService;
import com.slamperboom.backend.dataLogic.services.TaxService;
import com.slamperboom.backend.dataLogic.views.taxes.TaxCreateView;
import com.slamperboom.backend.frontendDTO.TaxFactorCreateDTO;
import com.slamperboom.backend.frontendDTO.TaxFactorDTO;
import com.slamperboom.backend.frontendDTO.TaxDTO;
import com.slamperboom.backend.frontendDTO.TaxValueDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Tax data API", description = "Manage tax values and other related data")
@RestController
@RequestMapping("taxes")
@RequiredArgsConstructor
public class TaxController {
    private final TaxService taxService;
    private final IControllerDataService dataService;

    @Operation(
            summary = "Get values for tax or factor",
            parameters = {
                    @Parameter(name = "taxname", description = "Tax name for which you need to get values", required = true)
            }
    )
    @GetMapping("values")
    public List<TaxDTO> getTaxByName(@RequestParam(name = "taxname") String taxName){
        return taxService.getValuesForTax(taxName);
    }

    @Operation(
            summary = "Get names of all available taxes"
    )
    @GetMapping("tax/names")
    public List<String> getTaxNames(){
        return taxService.getTaxNames();
    }

    @Operation(
            summary = "Get names of all available factors"
    )
    @GetMapping("factor/names")
    public List<String> getFactorNames(){
        return taxService.getFactorNames();
    }

    @Operation(
            summary = "Get all tax-factor links for tax",
            parameters = {
                    @Parameter(name = "taxname", description = "Tax name for which you need to get tax-factor links", required = true)
            }
    )
    @GetMapping("tax/factors")
    public List<TaxFactorDTO> getFactorsNamesForTax(@RequestParam(name = "taxname") String taxName) {
        return taxService.getFactorsNamesForTax(taxName);
    }

    @Operation(
            summary = "Save changed tax entity"
    )
    @PostMapping("save")
    public void saveTax(@RequestBody TaxDTO taxDTO){
        taxService.saveTaxValue(taxDTO);
    }

    @Operation(
            summary = "Add tax-factor link"
    )
    @PostMapping("tax/factor/add")
    public void addTaxFactorLink(@RequestBody TaxFactorCreateDTO taxFactorLinkDTO){
        taxService.saveTaxFactorLink(taxFactorLinkDTO);
    }

    @Operation(
            summary = "Add new data for tax",
            parameters = {
                    @Parameter(name = "type", description = "Type of data that you want to add (TAX or FACTOR)")
            }
    )
    @PostMapping("add")
    public void addTaxValues(@RequestParam("type") String type, @RequestBody TaxValueDTO taxValueDTO){
        taxService.addTaxValue(new TaxCreateView(taxValueDTO.taxName(), TaxType.valueOf(type), taxValueDTO.date(), taxValueDTO.value()));
    }

    @Operation(
            summary = "Add data for tax via file",
            description = """
                     File that you provide must have .csv extension. First line should be header
                     Date;Value
                     following lines should contain data like this:
                     2023-12-31;100.5""",
            parameters = {
                    @Parameter(name = "taxname"),
                    @Parameter(name = "type", description = "Type of tax (TAX or FACTOR)")
            }
    )
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

    @Operation(
            summary = "Delete tax value by its id",
            parameters = {
                    @Parameter(name = "recordid", description = "Id of value you want to delete")
            }
    )
    @DeleteMapping("delete")
    public void deleteTaxValue(@RequestParam("recordid") String id){
        taxService.deleteTaxValue(Long.parseLong(id));
    }

    @Operation(
            summary = "Delete tax-factor link",
            parameters = {
                    @Parameter(name = "linkid", description = "Id of tax-factor link to delete")
            }
    )
    @DeleteMapping("tax/factor/delete")
    public void deleteTaxFactorLink(@RequestParam("linkid") String id){
        taxService.deleteTaxFactorLink(Long.parseLong(id));
    }
}
