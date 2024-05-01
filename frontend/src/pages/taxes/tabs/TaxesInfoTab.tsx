import { useCallback, useEffect, useState } from "react";
import { useTaxesApi } from "../../../hooks/api/useTaxesApi";
import { TaxDTO } from "../../../DTOs/TaxDTO";
import { Box, Button, Dialog, DialogActions, DialogContentText, DialogTitle, Divider, FormControl, IconButton, InputLabel, MenuItem, Select, Typography } from "@mui/material";
import { DataGrid} from "@mui/x-data-grid";
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import { TaxFactorDTO } from "../../../DTOs/TaxFactorDTO";
import { innerStorageMembers, useUtility } from "../../../hooks/useUtility";
import { TaxLineChart } from "../../TaxLineChart";

export function TaxesInfoTab(){
    const taxesApi = useTaxesApi();
    const utility = useUtility();
    const [taxes, setTaxes] = useState<string[]>([]);
    const [currentTax, setCurrentTax] = 
        useState<string>(localStorage.getItem(innerStorageMembers.lastTax) ?? "");
    const [values, setValues] = useState<TaxDTO[]>([]);
    const [factors, setFactors] = useState<TaxFactorDTO[]>([]);
    const [factorsToAdd, setFactorsToAdd] = useState<string[]>([]);
    const [factorToLink, setFactorToLink] = useState("");

    const [open, setOpen] = useState(false);
    const [dialogTitle, setDialogTitle] = useState("");
    const [dialogContent, setDialogContent] = useState("");
    const [handleDialog, setHandleDialog] = useState<() => void>();

    const linkFactor = useCallback(() => {
        taxesApi.add.addFactorLink({
            taxName: currentTax,
            factorName: factorToLink
        }).then(() => {
            window.location.reload();
        })
    }, [taxesApi, currentTax, factorToLink]);

    useEffect(() => {
        taxesApi.get.getTaxesNames().then((result) => {
            setTaxes(result);
        })
    }, [taxesApi]);

    useEffect(() => {
        taxesApi.get.fetchValuesForTax(currentTax).then((result) => {
            setValues(result);
            taxesApi.get.getFactorsForTax(currentTax).then((result) => {
                setFactors(result);
                const taxFactors = Object.entries(result).map(([key, val]) => val.factorName);
                taxesApi.get.getFactorsNames().then((innerResult) => {
                    setFactorsToAdd(innerResult.concat(taxes.filter((v, i) => {return currentTax !== v})).filter((v, i) => {return !taxFactors.includes(v)}));
                })
            })
        })
    }, [currentTax, taxesApi, taxes]);

    return(
        <Box
        display="flex"
        flexDirection="column"
        justifyContent="center">
            <Dialog open={open}>
                <Box px={2}>
                <DialogTitle>{dialogTitle}</DialogTitle>
                <DialogContentText>{dialogContent}</DialogContentText>
                <DialogActions>
                    <Button variant="contained" onClick={() => setOpen(false)}>Нет</Button>
                    <Button onClick={() => {
                        if(handleDialog){
                            handleDialog();
                        }
                        setHandleDialog(() => {});
                        window.location.reload();
                    }}>Да</Button>
                </DialogActions>
                </Box>
            </Dialog>
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
                            localStorage.setItem(innerStorageMembers.lastTax, e.target.value); 
                            setCurrentTax(e.target.value)
                        }} 
                    >
                        <MenuItem value=''><em>Налог</em></MenuItem>
                        {Object.entries(taxes).map(([key, item]) => 
                            <MenuItem value={item} key={key}>{item}</MenuItem>)}
                    </Select>
                </FormControl>
            </Box>
            { (currentTax !== "" && taxes.includes(currentTax)) && 
            <Box
            display="flex"
            flexDirection="column"
            height="120vh">
                <Box
                    height="60vh">
                    <TaxLineChart values={values} taxName={currentTax}/>
                </Box>
                <Box display="flex"
                    flexDirection="row"
                    height="60vh"
                    alignItems="center">
                    
                    <Box
                        display="flex"
                        flexDirection="column"
                        justifyContent="center"
                        width="50%"
                        padding={2}
                        height="100%"
                    >
                        <Typography
                        align="center"
                        variant="h5">
                            Значения налога
                        </Typography>
                        <DataGrid columns={[
                            {
                                field: "date",
                                headerName: "Дата",
                                type: "date",
                                flex: 0.3,
                                editable: true,
                                valueGetter: (value: string | null) => value && utility.formatStringToDate(value),
                                valueFormatter: (value: Date | null) => value && utility.formatDateToString(value),
                                headerAlign: "center",
                                align: "center",
                            },
                            {
                                field: "value",
                                headerName: "Значение",
                                flex: 0.6,
                                type: "number",
                                editable: true,
                                headerAlign: "center",
                                align: "center",
                            },
                            {
                                field: "delete",
                                headerName: "",
                                flex: 0.1,
                                align: "center",
                                renderCell: (params) => {
                                    const handle = () => taxesApi.delete.deleteTaxValue(params.row.id);

                                    const onClick = () => {
                                        setDialogTitle("Удаление данных");
                                        setDialogContent("Вы подтверждаете удаление данных налога " + params.row.taxName + " за " + new Date(params.row.date).toLocaleDateString());
                                        setHandleDialog(() => handle);
                                        setOpen(true);
                                    }
                                    
                                    return(
                                        <IconButton onClick={onClick}>
                                            <DeleteOutlineIcon/>
                                        </IconButton>
                                    )
                                }
                            }
                        ]}
                        rows={values}
                        sx={{
                            width: "100%",
                            alignSelf: "center",
                            marginTop: 2
                        }}
                        disableRowSelectionOnClick
                        disableColumnMenu
                        hideFooter
                        processRowUpdate={(newRow, oldRow) => {
                            taxesApi.add.saveTaxInfo(newRow);
                            return newRow;
                        }}
                        />
                    </Box>
                    <Divider orientation="vertical"/>
                    <Box display="flex"
                        flexDirection="column"
                        justifyContent="center"
                        width="50%"
                        padding={2}
                        height="100%">
                        <Typography
                        align="center"
                        variant="h5">
                            Связанные факторы
                        </Typography>
                        <DataGrid columns={[
                            {
                                field: "id",
                                headerName: "Номер",
                                type: "number",
                                flex: 0.3,
                                editable: false,
                                headerAlign: "center",
                                align: "center",
                            },
                            {
                                field: "value",
                                headerName: "Фактор",
                                flex: 0.7,
                                editable: false,
                                headerAlign: "center",
                                align: "center",
                            },
                            {
                                field: "delete",
                                headerName: "",
                                flex: 0.1,
                                align: "center",
                                renderCell: (params) => {

                                    const handle = () => taxesApi.delete.deleteTaxFactorLink(params.row.recordId);

                                    const onClick = () => {
                                        setDialogTitle("Удаление связи");
                                        setDialogContent("Вы подтверждаете удаление связи налога " + params.row.tax + " с фактором " + params.row.value);
                                        setHandleDialog(() => handle);
                                        setOpen(true);
                                    }

                                    return(
                                        <IconButton onClick={onClick}>
                                            <DeleteOutlineIcon/>
                                        </IconButton>
                                    )
                                }
                            }
                        ]}
                        rows={factors.map((val, i) => ({id: i+1, value: val.factorName, recordId: val.id, tax: val.taxName}))}
                        sx={{
                            width: "100%",
                            alignSelf: "center",
                            margin: 2
                        }}
                        disableRowSelectionOnClick
                        disableColumnMenu
                        hideFooter
                        />
                        <Divider/>
                        <Typography
                        align="center"
                        variant="h5"
                        marginTop={2}>
                            Добавить связанный фактор
                        </Typography>
                        <Box
                            display="flex"
                            justifyContent="center"
                            my={3}>
                            <FormControl sx={{width: "20%", mx: 3}}>
                                <InputLabel id="select-label">Фактор</InputLabel>
                                <Select
                                    value={factorToLink}
                                    label="Фактор"
                                    labelId='select-label'
                                    onChange={(e) => {
                                        setFactorToLink(e.target.value)
                                    }} 
                                >
                                    <MenuItem value=''> <em>Фактор</em></MenuItem>
                                    {Object.entries(factorsToAdd).map(([key, item]) => 
                                        <MenuItem value={item} key={key}>{item}</MenuItem>)}
                                </Select>
                            </FormControl>
                            <Button
                            disabled={factorToLink === ""}
                            variant="contained"
                            onClick={linkFactor}
                            >
                                Связать
                            </Button>
                        </Box>
                    </Box>
                </Box>
                <Box
                marginTop={2}
                display="flex"
                justifyContent="center">
                <Button
                variant="contained"
                onClick={() => {
                    const handle = () => taxesApi.delete.deleteAll(currentTax).then(() => window.location.reload());
                    setDialogTitle("Удаление данных");
                    setDialogContent("Вы подтверждаете удаление всех данных налога " + currentTax);
                    setHandleDialog(() => handle);
                    setOpen(true);
                }}>
                    Удалить все данные по налогу
                </Button>
                </Box>
            </Box>
        }
        </Box>
    )
}