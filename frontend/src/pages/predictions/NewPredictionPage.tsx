import { Box, Button, CircularProgress, Dialog, DialogActions, DialogContent, DialogContentText, Divider, FormControl, Grid, InputLabel, MenuItem, Select, TextField, Typography } from '@mui/material';
import { SideMenuWrapper } from '../SideMenuWrapper';
import { usePredictionApi } from '../../hooks/api/usePredictionApi';
import { useTaxesApi } from '../../hooks/api/useTaxesApi';
import { ReactNode, useCallback, useEffect, useState } from 'react';
import { AlgorithmDTO } from '../../DTOs/AlgorithmDTO';
import { PredictionResultShortView } from './PredictionResultShortView';
import { PredictionResultDTO } from '../../DTOs/PredictionResultDTO';

export function NewPredictionPage() {
    const predictionApi = usePredictionApi();
    const taxesApi = useTaxesApi();
    const [methods, setMethods] = useState<AlgorithmDTO[]>([]);
    const [taxes, setTaxes] = useState<string[]>([]);
    const [currentMethod, setCurrentMethod] = useState("");
    const [currentTax, setCurrentTax] = useState("");

    const [description, setDescription] = useState("");
    const [parametersNode, setParametersNode] = useState<ReactNode>();
    const [resultNode, setResultNode] = useState<ReactNode>();
    const [parametersValues, setParametersValues] = useState<string[]>([]);
    const [requestProcessing, setRequestProcessing] = useState(false);
    const [buttonActive, setButtonActive] = useState(false);

    const makePrediction = useCallback(() => {
        if(currentMethod === "" || currentTax === ""){
            console.log("error");
        }else{
            setRequestProcessing(true);
            setResultNode(<></>);
            predictionApi.makePrediction({
                methodName: currentMethod,
                taxName: currentTax,
                params: parametersValues
            }).then((result) => {
                setRequestProcessing(false);
                setResultNode(<ResultsNode resultCode={result.resultCode} results={result.results} blockSetter={setResultNode}/>);
            }).catch((error) => {
                console.log(error);
                setRequestProcessing(false);
            });  
        }
    }, [parametersValues, currentMethod, currentTax, predictionApi]);

    useEffect(() => {
        predictionApi.getMethods().then((data) => setMethods(data)).catch(() => setMethods([]));
        taxesApi.get.getTaxesNames().then((data) => setTaxes(data)).catch(() => setTaxes([]));
    }, [predictionApi, taxesApi]);

    useEffect(() => {
        if(currentMethod === "" || currentTax === ""){
            setButtonActive(false);
        }else{
            setButtonActive(true);
        }
    }, [currentMethod, currentTax]);

    useEffect(() => {
        const descr = methods.find((e) => e.methodName === currentMethod)?.methodDescription;
        setDescription(descr ? descr : "");
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
    }, [currentMethod, methods, parametersValues]);

    return (
        <SideMenuWrapper>
            <Box
            display="flex"
            flexDirection="column"
            justifyContent="center">
                <Typography
                variant='h4'
                align="center"
                sx={{
                    marginBottom: 1,
                    width: "100%"
                }}>
                    Создать новый прогноз
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
                        На этой странице вы можете выполнить прогноз налога, представленного в базе данных программы, любым из имеющихся алгоритмов.
                        Для этого необходимо выбрать алгоритм и налог, а также заполнить необходимые для алгоритма параметры.
                        После выполнения прогноза можно сравнить получившийся прогноз с ранее выполненными прогнозами и принять решение о том, 
                        сохранять его или нет.
                    </Typography>
                </Box>
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
                    Описание алгоритма
                </Typography>
                {description !== "" ?
                <Box
                width="50%"
                alignSelf="center">
                    <Typography
                    sx={{
                        whiteSpace: 'break-spaces'
                    }}>
                        {description}
                    </Typography>
                </Box>
                :
                <Typography
                    color="gray"
                    variant='body1'
                    align='center'>
                        Для этого алгоритма описание не определено
                </Typography>
                }
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
                    }}
                    disabled={!buttonActive}>
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
                        error={val.length === 0}
                        helperText={val.length === 0 ? "Значение параметра не должно быть пустым" : ""}
                    />
            </Grid>
        </Grid>
    );
}

type ParameterItemProps = {
    paramDescription: string,
    onChange: (val: string) => void
}

function ResultsNode(props: ResultsNodeProps){
    const prediction = props.results[0];
    const otherResults = props.results.slice(1);
    const predictionApi = usePredictionApi();
    const [openDialog, setOpenDialog] = useState(false);
    const [dialogText, setDialogText] = useState("");

    const saveResult = useCallback((save: boolean) => {
        if(save){
            predictionApi.saveResult(props.resultCode, true).then(() => {
                setDialogText("Прогноз успешно сохранен");
                setOpenDialog(true);
            });
        }else{
            predictionApi.saveResult(props.resultCode, false).then(() => {
                props.blockSetter(<></>);
            });
        }
    }, [predictionApi, props, prediction]);

    return(
        <Box 
            display="flex"
            flexDirection="column"
        >
            <Dialog
                open={openDialog}
                onClose={() => window.location.reload()}>
                <DialogContent>
                    <DialogContentText>
                        {dialogText}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => window.location.reload()}>Ок</Button>
                </DialogActions>
            </Dialog>
            <Box
            display="flex"
            justifyContent="center"
            margin={2}>
                <Button
                variant='contained'
                onClick={() => saveResult(true)}
                sx={{
                    mx: 1
                }}>
                    Сохранить прогноз
                </Button>
                <Button
                variant='contained'
                onClick={() => saveResult(false)}
                sx={{
                    mx: 1
                }}>
                    Отклонить прогноз
                </Button>
            </Box>
            <Box
            display="flex"
            flexDirection="row"
            sx={{
                margin: 2
            }}>
                <PredictionResultShortView result={prediction}/>
                <Divider orientation='vertical' sx={{mx: 2}}/>
                {otherResults.length > 0 ?
                <Box
                display="flex"
                flexDirection="row"
                width="50vw"
                sx={{
                    overflowX: "scroll"
                }}
                >
                    {Object.entries(otherResults).map(([key, item]) => 
                        <PredictionResultShortView result={item}/>
                    )}
                </Box>
                :
                <Box
                width="50vw"
                height="100%"
                display="flex"
                flexDirection="column"
                justifyContent="center">
                    <Typography
                    color="gray"
                    variant='body1'
                    align='center'>
                        Другие прогнозы для этого налога не выполнялись
                    </Typography>
                </Box>
                }
            </Box>
        
        </Box>
    )
}

type ResultsNodeProps = {
    resultCode: string,
    results: PredictionResultDTO[],
    blockSetter: (node: ReactNode) => void
}