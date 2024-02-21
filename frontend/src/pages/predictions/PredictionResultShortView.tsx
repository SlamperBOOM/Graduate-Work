import { Box, Divider, Grid, Typography } from '@mui/material';
import { MathErrorDTO, ParameterDTO, PredictionResultDTO, ValueDTO } from '../../DTOs/PredictionResultDTO';
import Scrollbars from 'react-custom-scrollbars-2';

export function PredictionResultShortView(props: PredictionResultShortViewProps) {
    const result = props.result;
    
    return (
        <Box
        sx={{
            minWidth:"25vw",
            mx: 2,
            height: "60vh",
            border: "solid",
            borderRadius: 3,
            borderWidth: 1,
            paddingTop: 2,
            backgroundColor: "#99ccff",
            display: "flex",
            flexDirection: "column"
        }}>
            <Typography
                variant='h4'
                align='center'
            >
                {result.methodName}
            </Typography>
            <Scrollbars
            autoHide
            autoHideTimeout={2000}
            autoHideDuration={500}>
            <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                padding: 3
            }}>
                <Box
                display="flex"
                flexDirection="column"
                maxHeight="60%"
                sx={{
                    marginBottom: 2
                }}>
                    <Typography
                    variant='body1'
                    align='center'
                    fontWeight="bold">
                        Прогнозные значения
                    </Typography>
                    <Grid container sx={{
                        fontWeight: "bold",
                        textAlign: "center"
                    }}>
                        <Grid item md={6}>
                            Дата
                        </Grid>
                        <Grid item md={6}>
                            Значение
                        </Grid>
                    </Grid>
                    <Scrollbars
                        autoHeight>
                    <Box
                    display="flex"
                    flexDirection="column"
                    sx={{
                        padding: 1,
                        backgroundColor: "white"
                    }}>
                    {Object.entries(result.predictionValues).map(([key, item]) => 
                        <ValueView value={item}/>
                    )}
                    </Box>
                    </Scrollbars>
                </Box>
                <Divider/>
                <Box
                display="flex"
                flexDirection="column"
                maxHeight="60%"
                sx={{
                    marginBottom: 2
                }}>
                    <Typography
                    variant='body1'
                    align='center'
                    fontWeight="bold">
                        Референсные значения
                    </Typography>
                    <Grid container sx={{
                        fontWeight: "bold",
                        textAlign: "center"
                    }}>
                        <Grid item md={6}>
                            Дата
                        </Grid>
                        <Grid item md={6}>
                            Значение
                        </Grid>
                    </Grid>
                    <Scrollbars
                        autoHeight>
                    <Box
                    display="flex"
                    flexDirection="column"
                    sx={{
                        padding: 1,
                        backgroundColor: "white"
                    }}>
                    {Object.entries(result.referenceValues).map(([key, item]) => 
                        <ValueView value={item}/>
                    )}
                    </Box>
                    </Scrollbars>
                </Box>
                <Divider/>
                <Typography
                    variant='body1'
                    align='center'
                    fontWeight="bold">
                    Математические ошибки
                </Typography>
                <Box
                display="flex"
                flexDirection="column"
                height="50%"
                sx={{
                    marginBottom: 2
                }}>
                    <Grid container sx={{
                        fontWeight: "bold",
                        textAlign: "center"
                    }}>
                        <Grid item md={4}>
                            Название
                        </Grid>
                        <Grid item md={5}>
                            Значение
                        </Grid>
                        <Grid item md={3}>
                            В сравнении
                        </Grid>
                    </Grid>
                    <Scrollbars
                        autoHeight>
                    <Box
                    display="flex"
                    flexDirection="column"
                    sx={{
                        padding: 1,
                        backgroundColor: "white"
                    }}>
                    {Object.entries(result.mathErrors).map(([key, item]) => 
                        <MathErrorView error={item}/>
                    )}
                    </Box>
                    </Scrollbars>
                </Box>
                <Divider/>
                <Typography
                variant='body1'
                align='center'
                fontWeight="bold">
                    Параметры прогноза
                </Typography>
                <Box
                display="flex"
                flexDirection="column"
                height="50%">
                    <Grid container sx={{
                        fontWeight: "bold",
                        textAlign: "center"
                    }}>
                        <Grid item md={5}>
                            Параметр
                        </Grid>
                        <Grid item md={7}>
                            Значение
                        </Grid>
                    </Grid>
                    <Scrollbars
                        autoHeight>
                    <Box
                    display="flex"
                    flexDirection="column"
                    sx={{
                        padding: 1,
                        backgroundColor: "white"
                    }}>
                    {Object.entries(result.parameters).map(([key, item]) => 
                        <ParameterView param={item}/>
                    )}
                    </Box>
                    </Scrollbars>
                </Box>
            </Box>
            </Scrollbars>
        </Box>
    );
};

function ValueView(props: ValueViewProps){
    return(
        <Grid container>
            <Grid item md={6}>
                {new Date(props.value.date).toLocaleDateString()}
            </Grid>
            <Grid item md={6}>
                {Math.round(props.value.value * 10000) / 10000}
            </Grid>
        </Grid>
    )
}

type ValueViewProps = {
    value: ValueDTO
}

function MathErrorView(props: MathErrorViewProps){
    const isBetter : Boolean|null = props.error.isBetter;

    return(
        <Grid container>
            <Grid item md={4}>
                {props.error.errorName}
            </Grid>
            <Grid item md={5}>
                {Math.round(props.error.value * 10000) / 10000}
            </Grid>
            <Grid item md={3}>
                {isBetter === null ? "" : isBetter ? "Лучше" : "Хуже"}
            </Grid>
        </Grid>
    )
}

type MathErrorViewProps = {
    error: MathErrorDTO
}

function ParameterView(props: ParameterViewProps){
    return(
        <Grid container>
            <Grid item md={5}>
                {props.param.paramName}
            </Grid>
            <Grid item md={7}>
                {Math.round(props.param.value * 10000) / 10000}
            </Grid>
        </Grid>
    )
}

type ParameterViewProps = {
    param: ParameterDTO
}

export type PredictionResultShortViewProps = {
    result: PredictionResultDTO
}