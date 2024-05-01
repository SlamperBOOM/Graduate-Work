import { Box, Button, Typography } from "@mui/material";
import { useRoute } from "../hooks/useRoute";

export function MainPage() {
    const route = useRoute();

    return (
        <Box
        margin={2}>
            <Typography
                align="center"
                variant="h2"
                fontWeight="bold">
                Tax prediction
            </Typography>
            <Box
                my={1}
                width="100%"
                display="flex"
                justifyContent="center">
                <Typography
                    align="center"
                    whiteSpace="break-spaces"
                    variant="h6"
                    sx={{
                        width: "50%"
                    }}>
                    С помощью моего приложения вы сможете прогнозировать налоговые/акцизные поступления и любые другие временные ряды.
                </Typography>
            </Box>
            <Box
                display="flex"
                flexDirection="row"
                justifyContent="center">
                <Box
                sx={{
                    border: 1,
                    borderRadius: 2,
                    padding: 2,
                    marginRight: 1,
                    width: "30%"
                }}>
                    <Typography
                        align="center"
                        variant="h5">
                        Получение прогноза
                    </Typography>
                    <Typography
                    align="center"
                    variant="body1"
                    whiteSpace="break-spaces">
                        На этой странице вы можете выполнить прогноз налогов с помощью различных алгоритмов
                    </Typography>
                    <Box
                    marginTop={2}
                    display="flex"
                    justifyContent="center">
                    <Button
                    variant="contained"
                    onClick={route.routeToPredictionPage}>
                        Перейти к получению прогноза
                    </Button>
                    </Box>
                </Box>
                <Box
                sx={{
                    border: 1,
                    borderRadius: 2,
                    padding: 2,
                    mx: 1,
                    width: "30%"
                }}>
                    <Typography
                        align="center"
                        variant="h5">
                        Просмотр результатов
                    </Typography>
                    <Typography
                    align="center"
                    variant="body1"
                    whiteSpace="break-spaces">
                        {"На этой странице вы можете посмотреть результаты выполненных прогнозов и связанные с ними параметры"}
                    </Typography>
                    <Box
                    marginTop={2}
                    display="flex"
                    justifyContent="center">
                    <Button
                    variant="contained"
                    onClick={route.routeToResultsPage}
                    sx={{alignSelf: "center"}}>
                        Перейти к просмотру результатов
                    </Button>
                    </Box>
                </Box>
                <Box
                sx={{
                    border: 1,
                    borderRadius: 2,
                    padding: 2,
                    marginLeft: 1,
                    width: "30%"
                }}>
                    <Typography
                        align="center"
                        variant="h5">
                        Просмотр данных
                    </Typography>
                    <Typography
                    align="center"
                    variant="body1"
                    whiteSpace="break-spaces">
                        {"На этой странице вы можете посмотреть данные по налогам и факторам, а также изменить или добавить новые данные"}
                    </Typography>
                    <Box
                    marginTop={2}
                    display="flex"
                    justifyContent="center">
                    <Button
                    variant="contained"
                    onClick={route.routeToTaxesPage}>
                        Перейти к просмотру данных
                    </Button>
                    </Box>
                </Box>
            </Box>
        </Box>
    );
};