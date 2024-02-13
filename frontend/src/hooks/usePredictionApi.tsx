import { useCallback, useMemo } from "react";
import { useApi } from "./useApi";
import { AlgorithmDTO } from "../DTOs/AlgorithmDTO";
import { PredictionRequestDTO } from "../DTOs/PredictionRequestDTO";
import { PredictionResultDTO } from "../DTOs/PredictionResultDTO";

export function usePredictionApi(){
    const baseAddress = "prediction/";
    const api = useApi();

    const getMethods = useCallback( async() => {
        return (await api.performGetRequest(baseAddress + "algorithms")) as AlgorithmDTO[];
    }, [api]);

    const makePrediction = useCallback(async (body: PredictionRequestDTO) => {
        return (await api.performPostRequest(baseAddress + "predict", body)) as PredictionResultDTO[];
    }, [api]);

    return useMemo(() => {
        return{
            getMethods,
            makePrediction
        }
    },[getMethods, makePrediction]);
}