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
                    marginRight: 1
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
                        
                    </Typography>
                    <Button
                    variant="contained">
                        Перейти к получению прогноза
                    </Button>
                </Box>
                <Box
                sx={{
                    border: 1,
                    borderRadius: 2,
                    padding: 2,
                    mx: 1
                }}>
                    <Typography
                        align="center"
                        variant="h5">
                        Просмотр результатов
                    </Typography>
                </Box>
                <Box
                sx={{
                    border: 1,
                    borderRadius: 2,
                    padding: 2,
                    marginLeft: 1
                }}>
                    <Typography
                        align="center"
                        variant="h5">
                        Просмотр данных
                    </Typography>
                </Box>
            </Box>
        </Box>
    );
};