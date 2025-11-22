package pe.edu.upc.mivivienda.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Costes_adicionales")
public class Costes_adicionales {

    @Id
    @Column(name = "Simulaciones_simulacion_id")
    // No uso @GeneratedValue porque en el ER esta PK también es FK a Simulaciones
    private int simulaciones_simulacion_id;

    @Column(name = "nombreCosto", nullable = false, length = 50)
    private String nombreCosto;

    @Column(name = "valor", nullable = false)
    private double valor;

    public Costes_adicionales() {}

    public Costes_adicionales(int simulaciones_simulacion_id, String nombreCosto, double valor) {
        this.simulaciones_simulacion_id = simulaciones_simulacion_id;
        this.nombreCosto = nombreCosto;
        this.valor = valor;
    }

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

