export type PredictionResultDTO = {
    taxName: string,
    methodName: string,
    referenceValues: ValueDTO[],
    predictionValues: ValueDTO[],
    mathErrors: MathErrorDTO[],
    parameters: ParameterDTO[]
}

export type ValueDTO = {
    date: number,
    value: number
}

export type MathErrorDTO = {
    methodName: string,
    errorName: string,
    value: number,
    isBetter?: boolean
}

export type ParameterDTO = {
    paramName: string,
    value: number
}