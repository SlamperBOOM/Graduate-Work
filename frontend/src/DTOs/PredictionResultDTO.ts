export type PredictionResultDTO = {
    taxName: string,
    methodName: string,
    referenceValues: {
        date: number,
        value: number
    }[],
    predictionValues: {
        date: number,
        value: number
    }[],
    mathErrors: {
        methodName: string,
        errorName: string,
        value: number,
        isBetter?: boolean
    },
    parameters: {
        paramName: string,
        value: number
    }
}