import { useCallback, useMemo } from "react";

export const innerStorageMembers = {
    lastTax: "data.lastTax",
    lastTab: "data.lastTab"
}

export function useUtility(){
    const formatDateToString = useCallback((date: Date) => {
        return [
            date.getDate().toString().padStart(2, '0'),
            (date.getMonth()+1).toString().padStart(2, '0'),
            date.getFullYear()
        ].join("-");
    }, []);

    const formatStringToDate = useCallback((date: string) => {
        const values = date.split("-").map((val) => parseInt(val));
        return new Date(values[2], values[1]-1, values[0]);
    }, []);
    
    return useMemo(() => {
        return{
            formatDateToString,
            formatStringToDate
        }
    }, [formatDateToString, formatStringToDate])
}