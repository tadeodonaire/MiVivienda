package pe.edu.upc.mivivienda.dtos;

public class Costes_adicionalesDTO {

    private int simulaciones_simulacion_id;
    private String nombreCosto;
    private double valor;

    public int getSimulaciones_simulacion_id() {
        return simulaciones_simulacion_id;
    }

    public void setSimulaciones_simulacion_id(int simulaciones_simulacion_id) {
        this.simulaciones_simulacion_id = simulaciones_simulacion_id;
    }

    public String getNombreCosto() {
        return nombreCosto;
    }

    public void setNombreCosto(String nombreCosto) {
        this.nombreCosto = nombreCosto;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}