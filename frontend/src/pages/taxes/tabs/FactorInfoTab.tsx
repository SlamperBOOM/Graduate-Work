import { useEffect, useState } from "react";
import { useTaxesApi } from "../../../hooks/api/useTaxesApi";
import { TaxDTO } from "../../../DTOs/TaxDTO";
import { Box, Button, Dialog, DialogActions, DialogContentText, DialogTitle, FormControl, IconButton, InputLabel, MenuItem, Select, Typography } from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import { innerStorageMembers, useUtility } from "../../../hooks/useUtility";
import { TaxLineChart } from "../../TaxLineChart";

export function FactorInfoTab(){
    const taxesApi = useTaxesApi();
    const utility = useUtility();
    const [factors, setFactors] = useState<string[]>([]);
    const [currentFactor, setCurrentFactor] = useState(localStorage.getItem(innerStorageMembers.lastTax) ?? "");
    const [values, setValues] = useState<TaxDTO[]>([]);

    const [open, setOpen] = useState(false);
    const [dialogTitle, setDialogTitle] = useState("");
    const [dialogContent, setDialogContent] = useState("");
    const [handleDialog, setHandleDialog] = useState<() => void>();

    useEffect(() => {
        taxesApi.get.getFactorsNames().then((result) => {
            setFactors(result);
        })
    }, [taxesApi]);

    useEffect(() => {
        taxesApi.get.fetchValuesForTax(currentFactor).then((result) => {
            setValues(result);
        })
    }, [currentFactor, taxesApi]);

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
                    <InputLabel id="select-label">Фактор</InputLabel>
                    <Select
                        value={currentFactor}
                        label="Фактор"
                        labelId='select-label'
                        onChange={(e) => {
                            localStorage.setItem(innerStorageMembers.lastTax, e.target.value); 
                            setCurrentFactor(e.target.value)
                        }} 
                    >
                        <MenuItem value=''> <em>Фактор</em></MenuItem>
                        {Object.entries(factors).map(([key, item]) => 
                            <MenuItem value={item} key={key}>{item}</MenuItem>)}
                    </Select>
                </FormControl>
            </Box>
            {(currentFactor !== "" && factors.includes(currentFactor)) &&
            <Box
            display="flex"
            flexDirection="column"
            height="120vh">
                <Box
                    height="60vh">
                    <TaxLineChart values={values} taxName={currentFactor}/>
                </Box>
                <Box display="flex"
                flexDirection="column"
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
                                flex: 0.4,
                                editable: true,
                                valueGetter: (value: string) => value && utility.formatStringToDate(value),
                                valueFormatter: (value : Date) => value && utility.formatDateToString(value),
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
                                        setDialogContent("Вы подтверждаете удаление данных фактора " + params.row.taxName + " за " + new Date(params.row.date).toLocaleDateString());
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
                </Box>
                <Box
                marginTop={2}
                display="flex"
                justifyContent="center">
                <Button
                variant="contained"
                onClick={() => {
                    setDialogTitle("Удаление данных");
                    setDialogContent("Вы подтверждаете удаление всех данных фактора " + currentFactor);
                    setHandleDialog(() => {
                        taxesApi.delete.deleteAll(currentFactor).then(() => window.location.reload());
                    });
                    setOpen(true);
                }}>
                    Удалить все данные по фактору
                </Button>
                </Box>
            </Box>
            }
        </Box>
    )
}