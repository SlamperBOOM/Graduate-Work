import { useCallback, useMemo } from "react";
import { useApi } from "./useApi";
import { AlgorithmDTO } from "../DTOs/AlgorithmDTO";
import { PredictionRequestDTO } from "../DTOs/PredictionRequestDTO";
import { PredictionResultDTO } from "../DTOs/PredictionResultDTO";
import { PredictionForFrontend } from "../DTOs/PredictionForFrontend";

export function usePredictionApi(){
    const baseAddress = "prediction/";
    const api = useApi();

    const getMethods = useCallback( async() => {
        return (await api.performGetRequest(baseAddress + "algorithms")) as AlgorithmDTO[];
    }, [api]);

    const makePrediction = useCallback(async (body: PredictionRequestDTO) => {
        return (await api.performPostRequest(baseAddress + "predict", body)) as PredictionForFrontend;
    }, [api]);

    const saveResult = useCallback(async (resultCode: string, body?: PredictionResultDTO) => {
        await api.performPostRequest(baseAddress + "confirm", {resultCode: resultCode, results: body ? [body] : []});
    }, [api]);

    return useMemo(() => {
        return{
            getMethods,
            makePrediction,
            saveResult
        }
    },[getMethods, makePrediction, saveResult]);
}