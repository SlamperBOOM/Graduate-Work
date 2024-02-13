import { Box, Button, Typography } from "@mui/material";
import { useRoute } from "../hooks/useRoute";


export function ErrorPage() {
    const route = useRoute();

    return (
        <Box sx={{
            justifyContent: "center",
            display: "flex",
            flexDirection: "column",
            height: "100vh",
            width: "100vw"
        }}>
            <Typography
                variant="h5"
                align="center">
                По указанному адресу ничего нет
            </Typography>
            <Button
                onClick={route.routeToMainPage}
                sx={{
                justifyContent: "center",
                borderRadius: 15,
                backgroundColor: "white",
                padding: 2,
                ':hover':{
                    bgcolor: '#E5F1FF'
                },
                textTransform: "none",
                }}
                color="inherit"
            >
                <Typography
                variant='body1'
                sx={{
                    fontWeight: "bold",
                    fontSize: 12
                }}
                >
                    Вернуться на главную
                </Typography>
            </Button>
        </Box>
    );
};