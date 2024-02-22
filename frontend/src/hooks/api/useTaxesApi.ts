import { useCallback, useMemo } from "react";
import { useApi } from "./useApi";
import { TaxDTO } from "../../DTOs/TaxDTO";
import { TaxFactorDTO } from "../../DTOs/TaxFactorDTO";

export function useTaxesApi(){
    const baseAddress = "taxes/";
    const api = useApi();

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
        return (await api.performGetRequest(baseAddress + "tax/factors?taxname=" + taxname)) as string[];
    }, [api]);

    return useMemo(() => {
        return{
            fetchValuesForTax,
            getTaxesNames,
            getFactorsForTax,
            getFactorsNames
        }
    },[fetchValuesForTax, getTaxesNames, getFactorsForTax, getFactorsNames]);
}