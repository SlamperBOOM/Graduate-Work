export type PredictionResultDTO = {
    taxName: string,
    methodName: string,
    referenceValues: ValueDTO[],
    predictionValues: ValueDTO[],
    mathErrors: MathErrorDTO[],
    parameters: ParameterDTO[]
}

export type ValueDTO = {
    date: string,
    value: number
}

export type MathErrorDTO = {
    methodName: string,
    errorName: string,
    value: number,
    isBetter: boolean | null
}

export type ParameterDTO = {
    paramName: string,
    value: number
}