import { Box, Button, FormControl, InputLabel, MenuItem, Select, TextField, Typography } from '@mui/material';
import { SideMenuWrapper } from '../SideMenuWrapper';
import { usePredictionApi } from '../../hooks/usePredictionApi';
import { useTaxesApi } from '../../hooks/useTaxesApi';
import { ReactNode, useCallback, useEffect, useState } from 'react';
import { AlgorithmDTO } from '../../DTOs/AlgorithmDTO';

export function NewPrediction() {
    const predictionApi = usePredictionApi();
    const taxesApi = useTaxesApi();
    const [methods, setMethods] = useState<AlgorithmDTO[]>([]);
    const [taxes, setTaxes] = useState<string[]>([]);
    const [currentMethod, setCurrentMethod] = useState("");
    const [currentTax, setCurrentTax] = useState("");
    const [parametersNode, setParametersNode] = useState<ReactNode>();
    const [parametersValues, setParametersValues] = useState<string[]>([]);

    const makePrediction = useCallback(() => {
        predictionApi.makePrediction({
            methodName: currentMethod,
            taxName: currentTax,
            params: parametersValues
        }).then((data) => {
            console.log(data);
        })
    }, [parametersValues, currentMethod, currentTax]);

    useEffect(() => {
        predictionApi.getMethods().then((data) => setMethods(data)).catch(() => setMethods([]));
        taxesApi.getTaxesNames().then((data) => setTaxes(data)).catch(() => setTaxes([]));
    }, [predictionApi, taxesApi]);

    useEffect(() => {
        const params = methods.find((e) => e.methodName === currentMethod)?.parameters;
        parametersValues.length = params ? params.length : 0;
        setParametersNode(
            <Box 
            display="flex"
            flexDirection="column">
                {params && Object.entries(params).map(([key, item]) => 
                <ParameterItem paramDescription={item} onChange={(val: string) => {
                    parametersValues[parseInt(key)] = val;
                }}/>)}
            </Box>
        )
    }, [currentMethod, methods]);

    return (
        <SideMenuWrapper>
            <Box>
                <Typography
                variant='h6'>
                    Создать новый прогноз
                </Typography>
                <FormControl sx={{width: "20%"}}>
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
                        {Object.entries(methods).map(([key, item]) => 
                            <MenuItem value={item.methodName} key={key}>{item.methodName}</MenuItem>)}
                    </Select>
                </FormControl>
                <FormControl sx={{width: "20%"}}>
                    <InputLabel id="select-label">Налог</InputLabel>
                    <Select
                        value={currentTax}
                        label="Налог"
                        labelId='select-label'
                        onChange={(e) => {
                            setCurrentTax(e.target.value)
                        }} 
                    >
                        <MenuItem value=''> <em>Налог</em></MenuItem>
                        {Object.entries(taxes).map(([key, item]) => 
                            <MenuItem value={item} key={key}>{item}</MenuItem>)}
                    </Select>
                </FormControl>
                <>
                {parametersNode}
                </>
                <Button
                onClick={makePrediction}>
                    Сделать прогноз
                </Button>
            </Box>
        </SideMenuWrapper>
    );
};

function ParameterItem(props: ParameterItemProps){
    const [val, setValue] = useState("");

    return(
        <Box 
        display="flex"
        flexDirection="row">
            <Typography>
                {props.paramDescription}
            </Typography>
            <TextField
                label="Значение"
                onChange={(e) => {
                    props.onChange(e.target.value)
                    setValue(e.target.value)
                }}
                variant='outlined'
                value={val}
            />
        </Box>
    );
}

type ParameterItemProps = {
    paramDescription: string,
    onChange: (val: string) => void
}