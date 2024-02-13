import axios from "axios";
import { useCallback, useMemo } from "react";

export function useApi(){
    const baseAddress = "http://localhost:8080/";

    const performGetRequest = useCallback( async(url: string) => {
        const response = await axios.get(baseAddress + url);
        return response.data;
    }, []);

    const performPostRequest = useCallback( async(url: string, body: unknown) => {
        const response = await axios.post(baseAddress + url, body);
        return response.data;
    }, []);

    return useMemo(() => {
        return{
            performGetRequest,
            performPostRequest
        }
    },[performGetRequest, performPostRequest]);
};