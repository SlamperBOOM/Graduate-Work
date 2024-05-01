import axios from "axios";
import { useCallback, useMemo } from "react";
import { ErrorResponse } from "../../errors/ErrorResponse";

export function useApi(){
    const baseAddress = "http://localhost:8080/";

    const performGetRequest = useCallback( async(url: string) => {
        return (await axios.get(baseAddress + url).catch((response) => {
            throw response.response.data as ErrorResponse;
        })).data;
    }, []);

    const performPostRequest = useCallback( async(url: string, body: unknown) => {
        return (await axios.post(baseAddress + url, body).catch((response) => {
            throw response.response.data as ErrorResponse;
        })).data;
    }, []);

    const performDeleteRequest = useCallback( async(url: string) => {
        return await axios.delete(baseAddress + url).catch((response) => {
            throw response.response.data as ErrorResponse;
        });
    }, []);

    return useMemo(() => {
        return{
            performGetRequest,
            performPostRequest,
            performDeleteRequest
        }
    },[performGetRequest, performPostRequest, performDeleteRequest]);
};