import { Box, Button, Divider, FormControl, InputLabel, MenuItem, Select, Tab, Tabs, Typography } from "@mui/material";
import { SideMenuWrapper } from "../SideMenuWrapper";
import { useEffect, useState } from "react";
import { TabContext, TabPanel } from "@mui/lab";
import { useTaxesApi } from "../../hooks/api/useTaxesApi";
import { DataGrid } from "@mui/x-data-grid";
import { TaxDTO } from "../../DTOs/TaxDTO";

export function TaxDataPage(props: TaxDataPageProps) {
    const taxesApi = useTaxesApi();
    const [currentTab, setCurrentTab] = useState("tax");

    useEffect(() => {

    }, [taxesApi]);

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
                    Данные по налогам
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
                        На этой странице вы можете посмотреть данные по налогам, факторам и связям налог-фактор, а также добавить новые данные,
                        изменить или удалить их.
                    </Typography>
                </Box>
                <TabContext value={currentTab}>
                    <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                        <Tabs value={currentTab} onChange={(e, v) => setCurrentTab(v)}>
                            <Tab label="Налоги" value="tax"/>
                            <Tab label="Факторы" value="factor"/>
                        </Tabs>
                    </Box>
                    <TabPanel value="tax">
                        <TaxesInfoTab/>
                    </TabPanel>
                    <TabPanel value="factor">
                        <FactorInfoTab/>
                    </TabPanel>
                </TabContext>
            </Box>
        </SideMenuWrapper>
    );
};

export type TaxDataPageProps = {

}

function TaxesInfoTab(){
    const taxesApi = useTaxesApi();
    const [taxes, setTaxes] = useState<string[]>([]);
    const [currentTax, setCurrentTax] = useState("");
    const [values, setValues] = useState<TaxDTO[]>([]);
    const [factors, setFactors] = useState<string[]>([]);
    const [factorsToAdd, setFactorsToAdd] = useState<string[]>([]);
    const [factorToLink, setFactorToLink] = useState("");

    useEffect(() => {
        taxesApi.getTaxesNames().then((result) => {
            setTaxes(result);
        })
    }, [taxesApi]);

    useEffect(() => {
        taxesApi.fetchValuesForTax(currentTax).then((result) => {
            setValues(result);
        })
        taxesApi.getFactorsForTax(currentTax).then((result) => {
            setFactors(result);
        })
        taxesApi.getFactorsNames().then((result) => {

        })
    }, [currentTax, taxesApi]);

    return(
        <Box
        display="flex"
        flexDirection="column"
        justifyContent="center">
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
                            setCurrentTax(e.target.value)
                        }} 
                    >
                        <MenuItem value=''> <em>Налог</em></MenuItem>
                        {Object.entries(taxes).map(([key, item]) => 
                            <MenuItem value={item} key={key}>{item}</MenuItem>)}
                    </Select>
                </FormControl>
            </Box>
            { currentTax !== "" && 
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
                    rows={values}
                    sx={{
                        width: "100%",
                        alignSelf: "center",
                        marginTop: 2
                    }}
                    disableRowSelectionOnClick
                    disableColumnMenu
                    hideFooter
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
                            headerName: "Значение",
                            flex: 0.7,
                            editable: false,
                            headerAlign: "center",
                            align: "center",
                        }
                    ]}
                    rows={factors.map((val, i) => ({id: i+1, value: val}))}
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
                        >
                            Связать
                        </Button>
                    </Box>
                </Box>
            </Box>
        }
        </Box>
    )
}

function FactorInfoTab(){
    const taxesApi = useTaxesApi();
    const [factors, setFactors] = useState<string[]>([]);
    const [currentFactor, setCurrentFactor] = useState("");
    const [values, setValues] = useState<TaxDTO[]>([]);

    useEffect(() => {
        taxesApi.getFactorsNames().then((result) => {
            setFactors(result);
        })
    }, [taxesApi]);

    useEffect(() => {
        taxesApi.fetchValuesForTax(currentFactor).then((result) => {
            setValues(result);
        })
    }, [currentFactor, taxesApi]);

    return(
        <Box
        display="flex"
        flexDirection="column"
        justifyContent="center">
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
                            setCurrentFactor(e.target.value)
                        }} 
                    >
                        <MenuItem value=''> <em>Фактор</em></MenuItem>
                        {Object.entries(factors).map(([key, item]) => 
                            <MenuItem value={item} key={key}>{item}</MenuItem>)}
                    </Select>
                </FormControl>
            </Box>
            {currentFactor !== "" &&
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
                rows={values}
                sx={{
                    width: "100%",
                    alignSelf: "center",
                    marginTop: 2
                }}
                disableRowSelectionOnClick
                disableColumnMenu
                hideFooter
                />
            </Box>
            </Box>
            }
        </Box>
    )
}