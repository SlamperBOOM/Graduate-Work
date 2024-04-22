import { Box, Button, FormControl, Grid, InputLabel, MenuItem, Select, TextField, Typography } from "@mui/material";
import { useTaxesApi } from "../../../hooks/api/useTaxesApi";
import { ChangeEvent, useCallback, useEffect, useState } from "react";

export function AddValuesTab() {
    const taxesApi = useTaxesApi();
    const [currentType, setCurrentType] = useState("TAX");
    const [taxes, setTaxes] = useState<string[]>([]);
    const [factors, setFactors] = useState<string[]>([]);
    const [currentTax, setCurrentTax] = useState("");
    const [wayOfAdd, setWayOfAdd] = useState("manual");

    const [newTax, setNewTax] = useState("");
    const [newDate, setNewDate] = useState("");
    const [newValue, setNewValue] = useState("");
    const [file, setFile] = useState<File | undefined>();

    useEffect(() => {
        taxesApi.get.getTaxesNames().then((result) => {
            setTaxes(result);
        })
        taxesApi.get.getFactorsNames().then((result) => {
            setFactors(result);
        })
    }, [taxesApi]);

    const addValue = useCallback(() => {
        taxesApi.add.addTaxValue({
            date: newDate,
            taxName: currentTax === "" ? newTax : currentTax,
            value: parseFloat(newValue)
        }, currentType).then(() => window.location.reload());
    }, [currentType, newDate, newValue, currentTax, newTax, taxesApi]);

    const handleUpload = useCallback((e : ChangeEvent<HTMLInputElement>) => {
        // @ts-ignore: says that files could be null, but it's not
        const uploadedFile = e.target.files[0];
        if(uploadedFile && uploadedFile.type !== "text/csv"){
            setFile(undefined)
        }else{
            setFile(uploadedFile);
        }
    }, []);

    const addValuesViaFile = useCallback(() => {
        if(file){
            taxesApi.add.addTaxValueViaFile(currentTax, currentType, file);
        }
    }, [taxesApi, currentTax, currentType, file]);

    return (
        <Box
        display="flex"
        flexDirection="column"
        justifyContent="center">
            <Grid container
            display="flex"
            justifyContent="center"
            my={3}
            rowSpacing={2}>
                <Grid item md={6}>
                    <Box
                    height="100%"
                    width="100%"
                    textAlign="right"
                    display="flex"
                    alignItems="center"
                    justifyContent="flex-end">
                        <Typography>
                            Выберите тип добавляемых данных:
                        </Typography>
                    </Box>
                </Grid>
                <Grid  item md={6}>
                    <FormControl sx={{width: "50%", mx: 3}}>
                        <InputLabel id="select-label">Тип</InputLabel>
                        <Select
                            value={currentType}
                            label="Тип"
                            labelId='select-label'
                            onChange={(e) => {
                                setCurrentType(e.target.value)
                                setCurrentTax("");
                            }} 
                        >
                            <MenuItem value="TAX">Налог</MenuItem>
                            <MenuItem value="FACTOR">Фактор</MenuItem>
                        </Select>
                    </FormControl>
                </Grid>
                <Grid item md={6}>
                    <Box
                    height="100%"
                    width="100%"
                    textAlign="right"
                    display="flex"
                    alignItems="center"
                    justifyContent="flex-end">
                        <Typography>
                            Выберите {currentType === "TAX" ? "налог" : "фактор"} для добавления данных:
                        </Typography>
                    </Box>
                </Grid>
                <Grid  item md={6}>
                    <FormControl sx={{width: "50%", mx: 3}}>
                        <InputLabel id="select-label">{currentType === "TAX" ? "Налог" : "Фактор"}</InputLabel>
                        <Select
                            value={currentTax}
                            label={currentType === "TAX" ? "Налог" : "Фактор"}
                            labelId='select-label'
                            onChange={(e) => {
                                setCurrentTax(e.target.value)
                            }} 
                        >
                            <MenuItem value=''> <em>Новый {currentType === "TAX" ? "налог" : "фактор"}</em></MenuItem>
                            {currentType === "TAX" ? 
                                Object.entries(taxes).map(([key, item]) => 
                                <MenuItem value={item} key={key}>{item}</MenuItem>) :
                                Object.entries(factors).map(([key, item]) => 
                                <MenuItem value={item} key={key}>{item}</MenuItem>)}
                        </Select>
                    </FormControl>
                </Grid>
                {currentTax === '' &&
                <>
                    <Grid item md={6}>
                        <Box
                        height="100%"
                        width="100%"
                        textAlign="right"
                        display="flex"
                        alignItems="center"
                        justifyContent="flex-end">
                            <Typography>
                                Введите название нового {currentType === "TAX" ? "налога" : "фактора"}:
                            </Typography>
                        </Box>
                    </Grid>
                    <Grid item md={6}>
                        <TextField sx={{width: "50%", mx: 3}} 
                        value={newTax} 
                        onChange={(e) => setNewTax(e.target.value)}/>
                    </Grid>
                </>
                }
                <Grid item md={6}>
                    <Box
                    height="100%"
                    width="100%"
                    textAlign="right"
                    display="flex"
                    alignItems="center"
                    justifyContent="flex-end">
                        <Typography>
                            Выберите способ добавления данных:
                        </Typography>
                    </Box>
                </Grid>
                <Grid  item md={6}>
                    <FormControl sx={{width: "50%", mx: 3}}>
                        <InputLabel id="select-label">Способ добавления</InputLabel>
                        <Select
                            value={wayOfAdd}
                            label="Способ добавления"
                            labelId='select-label'
                            onChange={(e) => {
                                setWayOfAdd(e.target.value)
                            }} 
                        >
                            <MenuItem value='manual'>Вручную</MenuItem>
                            <MenuItem value='file'>Через файл</MenuItem>
                        </Select>
                    </FormControl>
                </Grid>
                {wayOfAdd === "manual" ?
                <>
                <Grid item md={6}>
                    <Box
                    height="100%"
                    width="100%"
                    textAlign="right"
                    display="flex"
                    alignItems="center"
                    justifyContent="flex-end">
                        <Typography>
                            Выберите дату:
                        </Typography>
                    </Box>
                </Grid>
                <Grid item md={6}>
                    <TextField sx={{width: "50%", mx: 3}} 
                    type="date"
                    value={newDate} 
                    onChange={(e) => setNewDate(e.target.value)}/>
                </Grid>
                <Grid item md={6}>
                    <Box
                    height="100%"
                    width="100%"
                    textAlign="right"
                    display="flex"
                    alignItems="center"
                    justifyContent="flex-end">
                        <Typography>
                            Введите значение, которое соответствует выбранной дате:
                        </Typography>
                    </Box>
                </Grid>
                <Grid item md={6}>
                    <TextField sx={{width: "50%", mx: 3}} 
                    type="number"
                    value={newValue} 
                    onChange={(e) => setNewValue(e.target.value)}/>
                </Grid>
                <Grid item md={12} display="flex" justifyContent="center">
                    <Button
                    variant="contained"
                    sx={{
                        width: "20%",
                    }}
                    onClick={addValue}>
                        Добавить значение
                    </Button>
                </Grid>
                </>
                :
                <>
                <Grid item md={12} display="flex" justifyContent="center">
                    <Box
                    width="50%"
                    sx={{
                        border: 1,
                        borderRadius: 2,
                        padding: 1
                    }}>
                        <Typography whiteSpace="break-spaces">
                            Принимаются файлы формата .csv с разделителем ";". Первая строка должна содержать названия столбцов.{"\n"}
                            Должно быть 2 столбца с датой и значением. Дата должна быть в формате "31.12.2023". Количество значений не ограничено.
                        </Typography>
                    </Box>
                </Grid>
                <Grid item md={12} display="flex" justifyContent="center">
                    <label htmlFor="upload-button">
                        <input
                            accept="text/csv"
                            style={{ display: 'none' }}
                            id="upload-button"
                            name="upload-button"
                            type="file"
                            onChange={handleUpload}/>
                        <Button
                            variant="contained"
                            component="span"
                        >
                            Загрузить файл
                        </Button>
                        <label>{file? file.name : ""}</label>
                    </label>
                </Grid>
                <Grid item md={12} display="flex" justifyContent="center">
                    <Button
                    variant="contained"
                    sx={{
                        width: "20%",
                    }}
                    onClick={addValuesViaFile}>
                        Добавить значения
                    </Button>
                </Grid>
                </>
                }
            </Grid>
        </Box>
    );
};