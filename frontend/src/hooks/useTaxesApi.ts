import { useCallback, useMemo } from "react";
import { useApi } from "./useApi";
import { TaxDTO } from "../DTOs/TaxDTO";

export function useTaxesApi(){
    const baseAddress = "taxes/";
    const api = useApi();

    const fetchValuesForTax = useCallback( async(taxName: string) => {
        return (await api.performGetRequest(baseAddress + "values?taxname=" + taxName)) as TaxDTO[];
    }, [api]);

    const getTaxesNames = useCallback(async() => {
        return (await api.performGetRequest(baseAddress + "names")) as string[];
    }, [api]);

    return useMemo(() => {
        return{
            fetchValuesForTax,
            getTaxesNames
        }
    },[fetchValuesForTax, getTaxesNames]);
}