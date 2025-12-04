package pe.edu.upc.mivivienda.dtos;

import java.util.List;

public class SimulacionConCronogramaResponse {

    private SimulacionesDTO simulacion;
    private List<Simulacion_CronogramaDTO> cronograma;

    // --- NUEVOS CAMPOS ---
    private double van;        // en moneda de la simulación
    private Double tirPeriodo;  // % por periodo
    private Double tcea;        // % anual (TCEA)

    public Double getTirPeriodo() {
        return tirPeriodo;
    }

    public void setTirPeriodo(Double tirPeriodo) {
        this.tirPeriodo = tirPeriodo;
    }

    public Double getTcea() {
        return tcea;
    }

    public void setTcea(Double tcea) {
        this.tcea = tcea;
    }

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

    public double getVan() {
        return van;
    }

    public void setVan(double van) {
        this.van = van;
    }

}
