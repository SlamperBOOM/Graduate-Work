import { Box, Button, Divider, Typography } from '@mui/material';
import { ResultDTO } from '../../DTOs/ResultDTO';
import { LineChart } from '@mui/x-charts';
import { DataGrid } from "@mui/x-data-grid";
import { useCallback } from 'react';
import { useUtility } from '../../hooks/useUtility';

export function PredictionResultView(props : PredictionResultViewProps) {
    const result = props.result;
    const utils = useUtility();

    const exportToXML = useCallback(() => {
        const resultCopy = result;
        resultCopy.predictionValues.map((val, i) => {val.date = utils.formatDate(new Date(parseInt(val.date)))});
        resultCopy.referenceValues.map((val, i) => {val.date = utils.formatDate(new Date(parseInt(val.date)))});
        const textFile = new Blob([JSON.stringify(resultCopy, null, 2)], {type: 'application/json'});

        const element = document.createElement("a");
        element.href = URL.createObjectURL(textFile);
        element.download = resultCopy.taxName + "_" + resultCopy.methodName + "_prediction.json";
        element.click();
    }, [result, utils]);

    return (
        <Box
        sx={{
            minWidth: "60vw",
            my: 2,
            padding: 2,
            display: "flex",
            flexDirection: "column",
            height: "170vh"
        }}>
            <Typography
                variant='h4'
                align='center'
            >
                {result.methodName}
            </Typography>
            <Button
            variant='contained'
            onClick={exportToXML}
            sx={{
                width: "20%",
                alignSelf: "center",
                my: 2
            }}>
                Экспортировать прогноз в .json
            </Button>
            <Typography
                variant='h5'
                align='center'
                sx={{my: 2}}
            >
                Значения
            </Typography>
            <LineChart
                xAxis={[{
                    data: result.predictionValues.map((v) => new Date(v.date)),
                    scaleType: "time"
                }]}
                series={[{
                    data: result.predictionValues.map((v) => v.value),
                    label: "Прогнозные значения",
                    color: "blue"
                },{
                    data: result.referenceValues.map((v) => v.value),
                    label: "Оригинальные значения",
                    color: "red"
                }]}
                sx={{
                    alignSelf: "center",
                    backgroundColor: 'white',
                    borderRadius: 2
                }}
            />
            <Box
            display="flex"
            flexDirection="row"
            height="50vh"
            width="100%"
            alignSelf="center"
            justifyContent="center"
            my={2}>
                <Box
                display="flex"
                flexDirection="column"
                width="75%"
                mx={2}>
                    <Typography
                    variant='h5'
                    align='center'>
                        Прогнозные значения
                    </Typography>
                    <DataGrid columns={[
                        {
                            field: "date",
                            headerName: "Дата",
                            type: "date",
                            flex: 0.4,
                            editable: false,
                            valueGetter: ({value}) => value && new Date(value),
                            headerAlign: "center",
                            align: "center",
                        },
                        {
                            field: "value",
                            headerName: "Значение",
                            flex: 0.6,
                            type: "number",
                            editable: false,
                            headerAlign: "center",
                            align: "center",
                        }
                    ]}
                    rows={result.predictionValues}
                    getRowId={(val) => val.date}
                    sx={{
                        width: "100%",
                        alignSelf: "center",
                        minHeight: "90%"
                    }}
                    disableRowSelectionOnClick
                    disableColumnMenu
                    hideFooter
                    />
                </Box>
                <Box
                display="flex"
                flexDirection="column"
                width="75%"
                mx={2}>
                    <Typography
                    variant='h5'
                    align='center'>
                        Оригинальные значения
                    </Typography>
                    <DataGrid columns={[
                        {
                            field: "date",
                            headerName: "Дата",
                            type: "date",
                            flex: 0.4,
                            editable: false,
                            valueGetter: ({value}) => value && new Date(value),
                            headerAlign: "center",
                            align: "center",
                        },
                        {
                            field: "value",
                            headerName: "Значение",
                            flex: 0.6,
                            type: "number",
                            editable: false,
                            headerAlign: "center",
                            align: "center",
                        }
                    ]}
                    rows={result.referenceValues}
                    getRowId={(val) => val.date}
                    sx={{
                        width: "100%",
                        alignSelf: "center",
                        minHeight: "90%"
                    }}
                    disableRowSelectionOnClick
                    disableColumnMenu
                    hideFooter
                    />
                </Box>
            </Box>
            <Divider/>
            <Box
            display="flex"
            flexDirection="row"
            height="40vh"
            width="100%"
            justifyContent="center"
            my={3}>
                <Box
                display="flex"
                flexDirection="column"
                height="40vh"
                width="75%"
                alignSelf="center"
                justifyContent="center"
                mx={2}>
                    <Typography
                    variant='h5'
                    align='center'>
                        Математические ошибки
                    </Typography>
                    <DataGrid columns={[
                        {
                            field: "errorName",
                            headerName: "Название",
                            flex: 0.3,
                            editable: false,
                            headerAlign: "center",
                            align: "center",
                        },
                        {
                            field: "value",
                            headerName: "Значение",
                            flex: 0.4,
                            type: "number",
                            editable: false,
                            headerAlign: "center",
                            align: "center",
                        },
                        {
                            field: "isBetter",
                            headerName: "Лучше/хуже",
                            flex: 0.3,
                            editable: false,
                            headerAlign: "center",
                            align: "center",
                            valueGetter: ({value}) => value && (value === null ? "" : value ? "Лучше" : "Хуже"),
                        }
                    ]}
                    rows={result.mathErrors.map((v, i) => ({
                        id: i,
                        errorName: v.errorName,
                        value: v.value,
                        isBetter: v.isBetter
                    }))}
                    getRowId={(val) => val.id}
                    sx={{
                        width: "100%",
                        alignSelf: "center",
                        minHeight: "100%"
                    }}
                    disableRowSelectionOnClick
                    disableColumnMenu
                    hideFooter
                    />
                </Box>
                <Box
                display="flex"
                flexDirection="column"
                height="40vh"
                width="75%"
                alignSelf="center"
                justifyContent="center"
                mx={2}>
                    <Typography
                    variant='h5'
                    align='center'>
                        Параметры прогноза
                    </Typography>
                    <DataGrid columns={[
                        {
                            field: "param",
                            headerName: "Параметр",
                            flex: 0.5,
                            editable: false,
                            headerAlign: "center",
                            align: "center",
                        },
                        {
                            field: "value",
                            headerName: "Значение",
                            flex: 0.5,
                            type: "number",
                            editable: false,
                            headerAlign: "center",
                            align: "center",
                        }
                    ]}
                    rows={result.parameters.map((v, i) => ({
                        id: i,
                        param: v.paramName,
                        value: v.value
                    }))}
                    getRowId={(val) => val.id}
                    sx={{
                        width: "100%",
                        alignSelf: "center",
                        minHeight: "100%"
                    }}
                    disableRowSelectionOnClick
                    disableColumnMenu
                    hideFooter
                    />
                </Box>
            </Box>
        </Box>
    );
};

export type PredictionResultViewProps = {
    result: ResultDTO
}