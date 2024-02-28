import axios from "axios";
import { useCallback, useMemo } from "react";

export function useApi(){
    const baseAddress = "http://localhost:8080/";

    const performGetRequest = useCallback( async(url: string) => {
        return (await axios.get(baseAddress + url).catch(() => {return {data: []}})).data;
    }, []);

    const performPostRequest = useCallback( async(url: string, body: unknown) => {
        return (await axios.post(baseAddress + url, body).catch(() => {return {data: []}})).data;
    }, []);

    const performDeleteRequest = useCallback( async(url: string) => {
        return await axios.delete(baseAddress + url);
    }, []);

    return useMemo(() => {
        return{
            performGetRequest,
            performPostRequest,
            performDeleteRequest
        }
    },[performGetRequest, performPostRequest, performDeleteRequest]);
};