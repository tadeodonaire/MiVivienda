package pe.edu.upc.mivivienda.dtos;

import java.util.List;

public class SimulacionConCronogramaResponse {

    private SimulacionesDTO simulacion;
    private List<Simulacion_CronogramaDTO> cronograma;

    // --- NUEVOS CAMPOS ---
    private double van;        // en moneda de la simulación
    private double tirAnual;   // TIR anual en %

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

    public double getTirAnual() {
        return tirAnual;
    }

    public void setTirAnual(double tirAnual) {
        this.tirAnual = tirAnual;
    }
}
