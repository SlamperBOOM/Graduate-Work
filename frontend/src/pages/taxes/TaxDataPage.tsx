import { Box, Tab, Tabs, Typography } from "@mui/material";
import { SideMenuWrapper } from "../SideMenuWrapper";
import { useEffect, useState } from "react";
import { TabContext, TabPanel } from "@mui/lab";
import { useTaxesApi } from "../../hooks/api/useTaxesApi";

export function TaxDataPage(props: TaxDataPageProps) {
    const taxesApi = useTaxesApi();
    const [currentTab, setCurrentTab] = useState("");

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
                            <Tab label="Связанные налоги и факторы" value="tax-factor"/>
                        </Tabs>
                    </Box>
                    <TabPanel value="tax">
                        <TaxesInfoTab/>
                    </TabPanel>
                    <TabPanel value="factor">
                        <FactorInfoTab/>
                    </TabPanel>
                    <TabPanel value="tax-factor">
                        <TaxFactorInfoTab/>
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

    return(
        <Box>

        </Box>
    )
}

function FactorInfoTab(){
    const taxesApi = useTaxesApi();
    const [factors, setFactors] = useState<string[]>([]);

    return(
        <Box>

        </Box>
    )
}

function TaxFactorInfoTab(){
    const taxesApi = useTaxesApi();
    const [factors, setFactors] = useState<string[]>([]);

    return(
        <Box>

        </Box>
    )
}