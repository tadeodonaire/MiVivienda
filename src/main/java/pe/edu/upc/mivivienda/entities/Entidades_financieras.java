package pe.edu.upc.mivivienda.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Entidades_financieras")
public class Entidades_financieras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int entidadFinanciera_id;

    @Column(name = "nombre", nullable = false, length = 25)
    private String nombre;

    @Column(name = "TEA", nullable = false)
    private double TEA;

    @Column(name = "seguroDesgravamen", nullable = false)
    private double seguroDesgravamen;

    @Column(name = "seguroInmueble", nullable = false)
    private double seguroInmueble;

    public Entidades_financieras() {}

    public Entidades_financieras(int entidadFinanciera_id, String nombre, double TEA, double seguroDesgravamen, double seguroInmueble) {
        this.entidadFinanciera_id = entidadFinanciera_id;
        this.nombre = nombre;
        this.TEA = TEA;
        this.seguroDesgravamen = seguroDesgravamen;
        this.seguroInmueble = seguroInmueble;
    }

    public int getEntidadFinanciera_id() {
        return entidadFinanciera_id;
    }

    public void setEntidadFinanciera_id(int entidadFinanciera_id) {
        this.entidadFinanciera_id = entidadFinanciera_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getTEA() {
        return TEA;
    }

    public void setTEA(double TEA) {
        this.TEA = TEA;
    }

    public double getSeguroDesgravamen() {
        return seguroDesgravamen;
    }

    public void setSeguroDesgravamen(double seguroDesgravamen) {
        this.seguroDesgravamen = seguroDesgravamen;
    }

    public double getSeguroInmueble() {
        return seguroInmueble;
    }

    public void setSeguroInmueble(double seguroInmueble) {
        this.seguroInmueble = seguroInmueble;
    }
}
