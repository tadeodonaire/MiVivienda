package pe.edu.upc.mivivienda.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Bonos_reglas")
public class Bonos_reglas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bonoRegla_id;

    @Column(name = "nombre", nullable = false, length = 25)
    private String nombre;

    @Column(name = "moneda",nullable = false)
    private String moneda;

    @Column(name = "precioMin", nullable = false)
    private double precioMin;

    @Column(name = "precioMax", nullable = false)
    private double precioMax;

    @Column(name = "ingresoMax", nullable = false)
    private double ingresoMax;

    @Column(name = "monto", nullable = false)
    private double monto;

    public Bonos_reglas() {}

    public Bonos_reglas(int bonoRegla_id, String nombre, String moneda, double precioMin, double precioMax, double ingresoMax, double monto) {
        this.bonoRegla_id = bonoRegla_id;
        this.nombre = nombre;
        this.moneda = moneda;
        this.precioMin = precioMin;
        this.precioMax = precioMax;
        this.ingresoMax = ingresoMax;
        this.monto = monto;
    }

    public int getBonoRegla_id() {
        return bonoRegla_id;
    }

    public void setBonoRegla_id(int bonoRegla_id) {
        this.bonoRegla_id = bonoRegla_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public double getPrecioMin() {
        return precioMin;
    }

    public void setPrecioMin(double precioMin) {
        this.precioMin = precioMin;
    }

    public double getPrecioMax() {
        return precioMax;
    }

    public void setPrecioMax(double precioMax) {
        this.precioMax = precioMax;
    }

    public double getIngresoMax() {
        return ingresoMax;
    }

    public void setIngresoMax(double ingresoMax) {
        this.ingresoMax = ingresoMax;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
