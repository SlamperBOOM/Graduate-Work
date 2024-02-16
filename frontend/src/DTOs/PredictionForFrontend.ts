import { PredictionResultDTO } from "./PredictionResultDTO"

export type PredictionForFrontend = {
    resultCode: string,
    results: PredictionResultDTO[];
}