import { Box, Button, CircularProgress, FormControl, Grid, InputLabel, MenuItem, Select, TextField, Typography } from '@mui/material';
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
    const [resultNode, setResultNode] = useState<ReactNode[]>();
    const [parametersValues, setParametersValues] = useState<string[]>([]);
    const [requestProcessing, setRequestProcessing] = useState(false);

    const makePrediction = useCallback(() => {
        if(currentMethod === "" || currentTax === "" || 
        (parametersValues.length > 0 && parametersValues.find((v) => {
            if(v.length === 0) return v;
            else return undefined;
        }))){
            //показать окно с ошибкой
            //либо добавить чекеры в сами поля, но это тяжко
            console.log("error");
        }else{
            setRequestProcessing(true);
            predictionApi.makePrediction({
                methodName: currentMethod,
                taxName: currentTax,
                params: parametersValues
            }).then((result) => {
                setRequestProcessing(false);
            });
        }
    }, [parametersValues, currentMethod, currentTax, predictionApi]);

    useEffect(() => {
        predictionApi.getMethods().then((data) => setMethods(data)).catch(() => setMethods([]));
        taxesApi.getTaxesNames().then((data) => setTaxes(data)).catch(() => setTaxes([]));
    }, [predictionApi, taxesApi]);

    useEffect(() => {
        const params = methods.find((e) => e.methodName === currentMethod)?.parameters;
        parametersValues.length = params ? params.length : 0;
        setParametersNode(
            <>
                {params && Object.entries(params).map(([key, item]) => 
                <ParameterItem paramDescription={item} onChange={(val: string) => {
                    parametersValues[parseInt(key)] = val;
                }}/>)}
            </>
        )
    }, [currentMethod, methods]);

    return (
        <SideMenuWrapper>
            <Box
            display="flex"
            flexDirection="column"
            justifyContent="center"
            width="100%">
                <Typography
                variant='h4'
                align="center"
                sx={{
                    marginTop: 3,
                    width: "100%"
                }}>
                    Создать новый прогноз
                </Typography>
                <Box
                display="flex"
                justifyContent="center"
                my={3}>
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
                            {Object.entries(methods).map(([key, item]) => 
                                <MenuItem value={item.methodName} key={key}>{item.methodName}</MenuItem>)}
                        </Select>
                    </FormControl>
                    <FormControl sx={{width: "20%", mx: 3}}>
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
                </Box>
                <Typography
                variant='h5'
                align='center'>
                    Список параметров
                </Typography>
                <Box>
                {parametersValues.length > 0 ? parametersNode : 
                <Box>
                    <Typography
                    color="gray"
                    variant='body1'
                    align='center'>
                        Для этого алгоритма параметры не определены
                    </Typography>
                </Box>}
                </Box>
                <Box 
                marginTop={3}
                display="flex"
                justifyContent="center">
                    <Button
                    onClick={makePrediction}
                    variant='contained'
                    sx={{
                        width: "20%"
                    }}>
                        Сделать прогноз
                    </Button>
                </Box>
                <Box
                display="flex"
                justifyContent="center"
                marginTop={3}
                >
                {requestProcessing && <CircularProgress/>}
                {resultNode}
                </Box>
            </Box>
        </SideMenuWrapper>
    );
};

function ParameterItem(props: ParameterItemProps){
    const [val, setValue] = useState("");

    return(
        <Grid container my={1}>
        <Grid
        item
        md={6}>
            <Box
            height="100%"
            width="100%"
            textAlign="right"
            display="flex"
            alignItems="center"
            justifyContent="flex-end">
                <Typography>
                    {props.paramDescription + ":"}
                </Typography>
            </Box>
        </Grid>
        <Grid
        item
        md={6}>
            <TextField
                    label="Значение"
                    sx={{
                        width: "75%",
                        mx: 1
                    }}
                    onChange={(e) => {
                        props.onChange(e.target.value)
                        setValue(e.target.value)
                    }}
                    variant='outlined'
                    value={val}
                />
        </Grid>
        </Grid>
    );
}

type ParameterItemProps = {
    paramDescription: string,
    onChange: (val: string) => void
}