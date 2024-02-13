import { useCallback, useMemo } from "react";
import { useNavigate } from "react-router-dom";

export const appRoutes = {
    mainPage: "/",
    prediction: "/prediction",
    taxes: "/taxes",
    results: "/results"
};

export function useRoute(){
    const navigate = useNavigate();

    const routeToMainPage = useCallback(() => {
        navigate(appRoutes.mainPage);
    }, [navigate]);

    const routeToPredictionPage = useCallback(() => {
        navigate(appRoutes.prediction);
    }, [navigate]);

    const routeToTaxesPage = useCallback(() => {
        navigate(appRoutes.taxes);
    }, [navigate]);

    const routeToResultsPage = useCallback(() => {
        navigate(appRoutes.results);
    }, [navigate]);

    return useMemo(() => {
        return{
            routeToMainPage,
            routeToPredictionPage,
            routeToTaxesPage,
            routeToResultsPage
        }
    }, [routeToMainPage, routeToPredictionPage, routeToTaxesPage, routeToResultsPage]);
}