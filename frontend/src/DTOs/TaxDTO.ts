export enum TaxType{
    TAX = "TAX",
    FACTOR = "FACTOR"
}

export type TaxDTO = {
    id: number,
    taxName: string,
    type: TaxType,
    date: string,
    value: number
}