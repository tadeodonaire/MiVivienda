package pe.edu.upc.mivivienda.dtos;

import jakarta.persistence.Column;

public class Entidades_financierasDTO {
    private int entidadFinanciera_id;
    private String nombre;
    private double valorCotizacionMax;
    private double cuotaInicialMin;
    private double TEAmin;
    private double TEAmax;
    private double precioMin;
    private double precioMax;
    private int plazoMax;
    private int plazoMin;
    private double seguroDesgravamen;
    private double seguroInmueble;

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
