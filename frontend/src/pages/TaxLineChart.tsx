import { TaxDTO } from '../DTOs/TaxDTO';
import { LineChart } from '@mui/x-charts/LineChart';

export function TaxLineChart(props: TaxLineChartProps) {
    return (
        <LineChart
                xAxis={[{
                    data: props.values.map((v) => new Date(v.date)),
                    scaleType: "time"
                }]}
                series={[{
                    data: props.values.map((v) => v.value),
                    label: props.taxName,
                    color: "blue"
                }]}
                sx={{
                    alignSelf: "center",
                    backgroundColor: 'white',
                    borderRadius: 2
                }}
                slotProps={{legend: {hidden: true}}}
                grid={{horizontal: true}}
            />
    );
}

export type TaxLineChartProps = {
    taxName: string,
    values: TaxDTO[]
}