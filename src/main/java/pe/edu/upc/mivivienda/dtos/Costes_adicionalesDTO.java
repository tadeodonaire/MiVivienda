package pe.edu.upc.mivivienda.dtos;

import jakarta.persistence.Column;
import pe.edu.upc.mivivienda.entities.Simulaciones;

public class Costes_adicionalesDTO {

    private int costesAdicional_id;
    private String nombreCosto;
    private double valor;
    private String tipo;           // "INICIAL" / "RECURRENTE"
    private Simulaciones simulaciones_simulacion_id;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCostesAdicional_id() {
        return costesAdicional_id;
    }

    public void setCostesAdicional_id(int costesAdicional_id) {
        this.costesAdicional_id = costesAdicional_id;
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

    public Simulaciones getSimulaciones_simulacion_id() {
        return simulaciones_simulacion_id;
    }

    public void setSimulaciones_simulacion_id(Simulaciones simulaciones_simulacion_id) {
        this.simulaciones_simulacion_id = simulaciones_simulacion_id;
    }
}