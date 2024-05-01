import { Box, Divider, FormControl, InputLabel, MenuItem, Select, Typography } from "@mui/material";
import { SideMenuWrapper } from "../SideMenuWrapper";
import { useTaxesApi } from "../../hooks/api/useTaxesApi";
import { ReactNode, useEffect, useState } from "react";
import { PredictionResultDTO } from "../../DTOs/PredictionResultDTO";
import { usePredictionApi } from "../../hooks/api/usePredictionApi";
import { PredictionResultView } from "./PredictionResultView";


export function ResultsPage(props: ResultsPageProps) {
    const taxesApi = useTaxesApi();
    const predictionApi = usePredictionApi();
    const [taxes, setTaxes] = useState<string[]>([]);
    const [currentTax, setCurrentTax] = useState("");
    const [results, setResults] = useState<PredictionResultDTO[]>([]);
    const [currentMethod, setCurrentMethod] = useState("");
    const [currentShowingResult, setCurrentShowingResult] = useState<ReactNode>();

    useEffect(() => {
        taxesApi.get.getTaxesNames().then((data) => setTaxes(data)).catch(() => setTaxes([]));
    }, [taxesApi]);

    useEffect(() => {
        if(currentTax !== ""){
            predictionApi.getResultsForTax(currentTax).then((results) => {setResults(results)})
        }else{
            setCurrentMethod("");
            setResults([]);
        }
    }, [currentTax, predictionApi]);

    useEffect(() => {
        if(currentMethod !== "" && currentTax !== ""){
            const result = results.find((v, i) => (v.methodName === currentMethod));
            if(result){
                setCurrentShowingResult(<PredictionResultView result={result}/>);
            }
        }else{
            setCurrentShowingResult(<></>);
        }
    }, [currentMethod, currentTax, results]);

    return (
        <SideMenuWrapper>
            <Box
            display="flex"
            flexDirection="column"
            justifyContent="center">
                <Typography
                align="center"
                variant="h4"
                sx={{
                    marginBottom: 1
                }}>
                    Результаты прогнозов
                </Typography>
                <Box
                width="50%"
                alignSelf="center"
                sx={{
                    border: 1,
                    borderRadius: 2,
                    padding: 1
                }}>
                    <Typography>
                        На этой странице вы можете посмотреть ранее выполненные прогнозы для каждого налога. Для этого необходимо выбрать налог,
                        а затем из предложенных вариантов выбрать алгоритм.
                    </Typography>
                </Box>
                <Box
                    display="flex"
                    justifyContent="center"
                    my={3}>
                    <FormControl sx={{width: "20%", mx: 3}}>
                        <InputLabel id="select-label">Налог</InputLabel>
                        <Select
                            value={currentTax}
                            label="Налог"
                            labelId='select-label'
                            onChange={(e) => {
                                setCurrentTax(e.target.value);
                                setCurrentMethod("");
                            }} 
                        >
                            <MenuItem value=''> <em>Налог</em></MenuItem>
                            {Object.entries(taxes).map(([key, item]) => 
                                <MenuItem value={item} key={key}>{item}</MenuItem>)}
                        </Select>
                    </FormControl>
                    <FormControl sx={{width: "20%", mx: 3}}>
                        <InputLabel id="select-label">Алгоритм</InputLabel>
                        <Select
                            value={currentMethod}
                            label="Алгоритм"
                            labelId='select-label'
                            onChange={(e) => {
                                setCurrentMethod(e.target.value)
                            }} 
                        >
                            <MenuItem value=''> <em>Алгоритм</em></MenuItem>
                            {Object.entries(results).map(([key, item]) => 
                                <MenuItem value={item.methodName} key={key}>{item.methodName}</MenuItem>)}
                        </Select>
                    </FormControl>
                </Box>
                <Divider/>
                <Box>
                    {
                        currentShowingResult
                    }
                </Box>
            </Box>
        </SideMenuWrapper>
    );
};

export type ResultsPageProps = {

}