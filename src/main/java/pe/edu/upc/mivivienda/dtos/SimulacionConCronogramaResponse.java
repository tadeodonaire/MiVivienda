package pe.edu.upc.mivivienda.dtos;

import java.util.List;

public class SimulacionConCronogramaResponse {

    private SimulacionesDTO simulacion;
    private List<Simulacion_CronogramaDTO> cronograma;

    public SimulacionesDTO getSimulacion() {
        return simulacion;
    }

    public void setSimulacion(SimulacionesDTO simulacion) {
        this.simulacion = simulacion;
    }

    public List<Simulacion_CronogramaDTO> getCronograma() {
        return cronograma;
    }

    public void setCronograma(List<Simulacion_CronogramaDTO> cronograma) {
        this.cronograma = cronograma;
    }
}
