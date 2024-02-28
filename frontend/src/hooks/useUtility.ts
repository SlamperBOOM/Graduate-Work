import { useCallback, useMemo } from "react";

export const innerStorageMembers = {
    lastTax: "data.lastTax",
    lastTab: "data.lastTab"
}

export function useUtility(){
    const formatDate = useCallback((date: Date) => {
        return [
            date.getFullYear(),
            (date.getMonth()+1).toString().padStart(2, '0'),
            date.getDate().toString().padStart(2, '0')
        ].join("-");
    }, []);
    
    return useMemo(() => {
        return{
            formatDate
        }
    }, [formatDate])
}