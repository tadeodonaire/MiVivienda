package pe.edu.upc.mivivienda.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Costes_adicionales")
public class Costes_adicionales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int costesAdicional_id;

    @Column(name = "nombreCosto", nullable = false, length = 50)
    private String nombreCosto;

    @Column(name = "valor", nullable = false)
    private double valor;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "Simulaciones_simulacion_id")
    private Simulaciones simulaciones_simulacion_id;

    public Costes_adicionales() {}

    public Costes_adicionales(int costesAdicional_id, String nombreCosto, double valor, Simulaciones simulaciones_simulacion_id) {
        this.costesAdicional_id = costesAdicional_id;
        this.nombreCosto = nombreCosto;
        this.valor = valor;
        this.simulaciones_simulacion_id = simulaciones_simulacion_id;
    }

    public Simulaciones getSimulaciones_simulacion_id() {
        return simulaciones_simulacion_id;
    }

    public void setSimulaciones_simulacion_id(Simulaciones simulaciones_simulacion_id) {
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

    public int getCostesAdicional_id() {
        return costesAdicional_id;
    }

    public void setCostesAdicional_id(int costesAdicional_id) {
        this.costesAdicional_id = costesAdicional_id;
    }
}

