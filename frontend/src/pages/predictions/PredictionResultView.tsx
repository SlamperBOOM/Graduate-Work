import { Box, Divider, Typography } from '@mui/material';
import { MathErrorDTO, PredictionResultDTO, ValueDTO } from '../../DTOs/PredictionResultDTO';

export function PredictionResultView(props: PredictionResultViewProps) {
    const result = props.result;
    
    return (
        <Box
        sx={{
            width:"20vw",
            height: "40vh"
        }}>
            <Typography
            variant='h6'
            align='center'>
                {result.methodName}
            </Typography>
            <Box>
                {/* выводить прогнозные значения в scrollbox*/}
            </Box>
            <Divider/>
            <Typography
            variant='body2'
            align='center'>
                Математические ошибки
            </Typography>
            <Box>
                
            </Box>
        </Box>
    );
};

function ValueView(value: ValueDTO){
    return(
        <Box>

        </Box>
    )
}

function MathErrorView(error: MathErrorDTO){
    return(
        <Box>

        </Box>
    )
}

export type PredictionResultViewProps = {
    result: PredictionResultDTO
}