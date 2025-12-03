package pe.edu.upc.mivivienda.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Entidades_financieras")
public class Entidades_financieras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int entidadFinanciera_id;

    @Column(name = "nombre", nullable = false, length = 25)
    private String nombre;

    @Column(name = "valorCotizacionMax")
    private double valorCotizacionMax;

    @Column(name = "cuotaInicialMin")
    private double cuotaInicialMin;

    @Column(name = "TEAmin", nullable = false)
    private double TEAmin;

    @Column(name = "TEAmax", nullable = false)
    private double TEAmax;

    @Column(name = "precioMin", nullable = false)
    private double precioMin;

    @Column(name = "precioMax", nullable = false)
    private double precioMax;

    @Column(name = "plazoMax", nullable = false)
    private int plazoMax;

    @Column(name = "plazoMin", nullable = false)
    private int plazoMin;

    @Column(name = "seguroDesgravamen", nullable = false)
    private double seguroDesgravamen;

    @Column(name = "seguroInmueble", nullable = false)
    private double seguroInmueble;

    public Entidades_financieras() {}

    public Entidades_financieras(int entidadFinanciera_id, String nombre, double valorCotizacionMax, double cuotaInicialMin, double TEAmin, double TEAmax, double precioMin, double precioMax, int plazoMax, int plazoMin, double seguroDesgravamen, double seguroInmueble) {
        this.entidadFinanciera_id = entidadFinanciera_id;
        this.nombre = nombre;
        this.valorCotizacionMax = valorCotizacionMax;
        this.cuotaInicialMin = cuotaInicialMin;
        this.TEAmin = TEAmin;
        this.TEAmax = TEAmax;
        this.precioMin = precioMin;
        this.precioMax = precioMax;
        this.plazoMax = plazoMax;
        this.plazoMin = plazoMin;
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

    public double getValorCotizacionMax() {
        return valorCotizacionMax;
    }

    public void setValorCotizacionMax(double valorCotizacionMax) {
        this.valorCotizacionMax = valorCotizacionMax;
    }

    public double getCuotaInicialMin() {
        return cuotaInicialMin;
    }

    public void setCuotaInicialMin(double cuotaInicialMin) {
        this.cuotaInicialMin = cuotaInicialMin;
    }

    public double getTEAmin() {
        return TEAmin;
    }

    public void setTEAmin(double TEAmin) {
        this.TEAmin = TEAmin;
    }

    public double getTEAmax() {
        return TEAmax;
    }

    public void setTEAmax(double TEAmax) {
        this.TEAmax = TEAmax;
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

    public int getPlazoMax() {
        return plazoMax;
    }

    public void setPlazoMax(int plazoMax) {
        this.plazoMax = plazoMax;
    }

    public int getPlazoMin() {
        return plazoMin;
    }

    public void setPlazoMin(int plazoMin) {
        this.plazoMin = plazoMin;
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
