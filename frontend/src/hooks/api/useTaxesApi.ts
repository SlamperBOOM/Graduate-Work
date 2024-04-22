import { useCallback, useMemo } from "react";
import { useApi } from "./useApi";
import { TaxDTO } from "../../DTOs/TaxDTO";
import { TaxFactorCreateDTO, TaxFactorDTO } from "../../DTOs/TaxFactorDTO";
import { TaxValueDTO } from "../../DTOs/TaxValueDTO";
import { useUtility } from "../useUtility";

export function useTaxesApi(){
    const baseAddress = "taxes/";
    const api = useApi();
    const utility = useUtility();

    const fetchValuesForTax = useCallback( async(taxName: string) => {
        return (await api.performGetRequest(baseAddress + "values?taxname=" + taxName)) as TaxDTO[];
    }, [api]);

    const getTaxesNames = useCallback(async() => {
        return (await api.performGetRequest(baseAddress + "tax/names")) as string[];
    }, [api]);

    const getFactorsNames = useCallback(async() => {
        return (await api.performGetRequest(baseAddress + "factor/names")) as string[];
    }, [api]);

    const getFactorsForTax = useCallback(async(taxname: string) => {
        return (await api.performGetRequest(baseAddress + "tax/factors?taxname=" + taxname)) as TaxFactorDTO[];
    }, [api]);

    const addFactorLink = useCallback(async(body: TaxFactorCreateDTO) => {
        await api.performPostRequest(baseAddress + "tax/factor/add", body);
    }, [api]);

    const saveTaxInfo = useCallback(async(body: TaxDTO) => {
        body.date = utility.formatDateToString(new Date(body.date));
        await api.performPostRequest(baseAddress + "save", body);
    }, [api, utility]);

    const addTaxValue = useCallback(async(body: TaxValueDTO, type: string) => {
        body.date = utility.formatDateToString(new Date(body.date));
        await api.performPostRequest(baseAddress + "add?type=" + type, body);
    }, [api, utility]);

    const addTaxValueViaFile = useCallback(async(taxName: string, type: string, file: File) => {
        const formData = new FormData();
        formData.append("file", file);
        await api.performPostRequest(baseAddress + "add/file?taxname="+taxName+"&type="+type, formData);
    }, [api]);

    const deleteTaxValue = useCallback(async(id: number) => {
        await api.performDeleteRequest(baseAddress + "delete?recordid=" + id);
    }, [api]);

    const deleteTaxFactorLink = useCallback(async(id: number) => {
        await api.performDeleteRequest(baseAddress + "tax/factor/delete?linkid=" + id);
    }, [api]);

    return useMemo(() => {
        return{
            get: {
                fetchValuesForTax,
                getTaxesNames,
                getFactorsForTax,
                getFactorsNames
            },
            add: {
                addFactorLink,
                saveTaxInfo,
                addTaxValue,
                addTaxValueViaFile
            },
            delete: {
                deleteTaxValue,
                deleteTaxFactorLink
            }
        }
    },[fetchValuesForTax, getTaxesNames, getFactorsForTax, 
        getFactorsNames, addFactorLink, saveTaxInfo, 
        addTaxValue, addTaxValueViaFile, deleteTaxValue, 
        deleteTaxFactorLink]);
}