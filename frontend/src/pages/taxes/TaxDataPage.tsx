import { Box, Tab, Tabs, Typography } from "@mui/material";
import { SideMenuWrapper } from "../SideMenuWrapper";
import { useEffect, useState } from "react";
import { TabContext, TabPanel } from "@mui/lab";
import { TaxesInfoTab } from "./tabs/TaxesInfoTab";
import { FactorInfoTab } from "./tabs/FactorInfoTab";
import { AddValuesTab } from "./tabs/AddValuesTab";
import { innerStorageMembers } from "../../hooks/useUtility";

export function TaxDataPage(props: TaxDataPageProps) {
    const [currentTab, setCurrentTab] = useState(localStorage.getItem(innerStorageMembers.lastTab) ?? "tax");

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
                    <Box sx={{ borderBottom: 1, borderColor: 'divider', marginTop: 2 }}>
                        <Tabs value={currentTab} onChange={(e, v) => {
                                setCurrentTab(v);
                                localStorage.setItem(innerStorageMembers.lastTab, v);
                            }}>
                            <Tab label="Налоги" value="tax"/>
                            <Tab label="Факторы" value="factor"/>
                            <Tab label="Добавить данные" value="addValues"/>
                        </Tabs>
                    </Box>
                    <TabPanel value="tax">
                        <TaxesInfoTab/>
                    </TabPanel>
                    <TabPanel value="factor">
                        <FactorInfoTab/>
                    </TabPanel>
                    <TabPanel value="addValues">
                        <AddValuesTab/>
                    </TabPanel>
                </TabContext>
            </Box>
        </SideMenuWrapper>
    );
};

export type TaxDataPageProps = {

}