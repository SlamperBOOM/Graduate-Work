import { useCallback, useMemo } from "react";
import { useApi } from "./useApi";
import { AlgorithmDTO } from "../../DTOs/AlgorithmDTO";
import { PredictionRequestDTO } from "../../DTOs/PredictionRequestDTO";
import { PredictionResultDTO } from "../../DTOs/PredictionResultDTO";
import { PredictionForFrontend } from "../../DTOs/PredictionForFrontend";
import { PredictionConfirmDTO } from "../../DTOs/PredictionConfirmDTO";

export function usePredictionApi(){
    const baseAddress = "prediction/";
    const api = useApi();

    const getMethods = useCallback( async() => {
        return (await api.performGetRequest(baseAddress + "algorithms")) as AlgorithmDTO[];
    }, [api]);

    const makePrediction = useCallback(async (body: PredictionRequestDTO) => {
        return (await api.performPostRequest(baseAddress + "predict", body)) as PredictionForFrontend;
    }, [api]);

    const saveResult = useCallback(async (resultCode: string, confirm: boolean) => {
        await api.performPostRequest(baseAddress + "confirm", {resultCode: resultCode, confirm: confirm} as PredictionConfirmDTO);
    }, [api]);

    const getResultsForTax = useCallback(async(taxName: string) => {
        return (await api.performGetRequest(baseAddress + "predicts/get?taxname=" + taxName)) as PredictionResultDTO[];
    }, [api]);

    return useMemo(() => {
        return{
            getMethods,
            makePrediction,
            saveResult,
            getResultsForTax
        }
    },[getMethods, makePrediction, saveResult, getResultsForTax]);
}