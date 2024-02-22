import { Box, Button, CssBaseline, Grid, Paper, Typography } from '@mui/material';
import { ReactElement, ReactNode, useEffect, useState } from 'react';
import { appRoutes, useRoute } from '../hooks/useRoute';

export function SideMenuWrapper(props : SideMenuWrapperProps) {
    const [isLoaded, setIsLoaded] = useState(false);
    const route = useRoute();

    useEffect(() => {
        setIsLoaded(true);
    }, []);

    return (
        <>
        {isLoaded &&
        <Grid container component="main" sx={{height: "100vh"}}>
            <CssBaseline />
            <Grid
                item
                md={2}
                component={Paper}
                elevation={2}
            >
                <Typography
                    variant='h6'
                    align='center'
                    sx={{
                        fontWeight: "bold",
                        marginTop: 5,
                        marginBottom: 5
                    }}>
                    Tax prediction
                </Typography>
                <Box
                    sx={{
                    mx: 5,
                    display: "flex",
                    flexDirection: 'column',
                    justifyContent: "flex-start"
                    }}
                >
                    <MenuItem text='Главная' onClick={route.routeToMainPage}/>
                    <MenuItem text='Выполнить прогноз' href={appRoutes.prediction} onClick={route.routeToPredictionPage}/>
                    <MenuItem text='Посмотреть результаты' href={appRoutes.results} onClick={route.routeToResultsPage}/>
                    <MenuItem text='Посмотреть данные' href={appRoutes.taxes} onClick={route.routeToTaxesPage}/>
                </Box>
            </Grid>
            <Grid
                item
                md={10}
                sx={{
                    display:"flex",
                    height:"100vh",
                    overflow:"auto"
                }}
            >
                <Box
                width="95%"
                mx={2}
                my={2}
                >
                    {props.children}
                </Box>
            </Grid>
        </Grid>
        }
        </>
    );
};

function MenuItem(props: MenuItemProps){
    const isCurrentPage = props.href ? window.location.href.includes(props.href) : false;

    return(
      <Button
        onClick={props.onClick}
        sx={{
          my: 1,
          justifyContent: "start",
          borderRadius: 15,
          backgroundColor: isCurrentPage ? "#E5F1FF" : "white",
          padding: 2,
          ':hover':{
              bgcolor: isCurrentPage ? "#b3d6ff" : '#E5F1FF'
          },
          textTransform: "none"
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
          {props.text}
        </Typography>
      </Button>
    )
}

type MenuItemProps = {
    text: string, 
    onClick: () => void,
    href?: string
}  

export type SideMenuWrapperProps = {
    children: string | number | boolean | ReactElement | Iterable<ReactNode> | null | undefined
}
  