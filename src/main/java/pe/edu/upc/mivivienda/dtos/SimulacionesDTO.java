package pe.edu.upc.mivivienda.dtos;

import pe.edu.upc.mivivienda.entities.Bonos_reglas;
import pe.edu.upc.mivivienda.entities.Costes_adicionales;
import pe.edu.upc.mivivienda.entities.Entidades_financieras;
import pe.edu.upc.mivivienda.entities.Propiedades;

import java.util.ArrayList;
import java.util.List;

public class SimulacionesDTO {
    private int simulacion_id;
    private double precioVenta;
    private double cuotaInicial;
    private double montoPrestamo; // calculado = precioVenta - cuotaInicial - bonoMonto (si aplica)
    private String moneda; // PEN / USD
    private Integer tiempoAnios; // años del crédito
    private Integer frecuenciaPago; // 12=mensual, 6=bimestral, etc.
    private Integer tipoAnio; // años 360 o 365
    private String tipoGracia; // "SIN_GRACIA" | "TOTAL" | "PARCIAL"
    private Integer cantidadGracia;
    private double seguroDesgravamen;
    private double seguroInmueble;
    private Propiedades propiedades_inmueble_id;
    private Entidades_financieras entidades_financieras_entidadFinanciera_id;
    private String tipoTasa; // "TEA"
    private double valorTasa; // TEA normalizada (fracción)
    private double cuotaFija; // calculada método francés
    private Boolean bonoAplica;
    private String bonoTipo;
    private double bonoMonto;
    private Bonos_reglas bono_Reglas_reglas_id;
    private List<Costes_adicionales> costos = new ArrayList<>();

    public int getSimulacion_id() {
        return simulacion_id;
    }

    public void setSimulacion_id(int simulacion_id) {
        this.simulacion_id = simulacion_id;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getCuotaInicial() {
        return cuotaInicial;
    }

    public void setCuotaInicial(double cuotaInicial) {
        this.cuotaInicial = cuotaInicial;
    }

    public double getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(double montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Integer getTiempoAnios() {
        return tiempoAnios;
    }

    public void setTiempoAnios(Integer tiempoAnios) {
        this.tiempoAnios = tiempoAnios;
    }

    public Integer getFrecuenciaPago() {
        return frecuenciaPago;
    }

    public void setFrecuenciaPago(Integer frecuenciaPago) {
        this.frecuenciaPago = frecuenciaPago;
    }

    public Integer getTipoAnio() {
        return tipoAnio;
    }

    public void setTipoAnio(Integer tipoAnio) {
        this.tipoAnio = tipoAnio;
    }

    public String getTipoGracia() {
        return tipoGracia;
    }

    public void setTipoGracia(String tipoGracia) {
        this.tipoGracia = tipoGracia;
    }

    public Integer getCantidadGracia() {
        return cantidadGracia;
    }

    public void setCantidadGracia(Integer cantidadGracia) {
        this.cantidadGracia = cantidadGracia;
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

    public Propiedades getPropiedades_inmueble_id() {
        return propiedades_inmueble_id;
    }

    public void setPropiedades_inmueble_id(Propiedades propiedades_inmueble_id) {
        this.propiedades_inmueble_id = propiedades_inmueble_id;
    }

    public Entidades_financieras getEntidades_financieras_entidadFinanciera_id() {
        return entidades_financieras_entidadFinanciera_id;
    }

    public void setEntidades_financieras_entidadFinanciera_id(Entidades_financieras entidades_financieras_entidadFinanciera_id) {
        this.entidades_financieras_entidadFinanciera_id = entidades_financieras_entidadFinanciera_id;
    }

    public String getTipoTasa() {
        return tipoTasa;
    }

    public void setTipoTasa(String tipoTasa) {
        this.tipoTasa = tipoTasa;
    }

    public double getValorTasa() {
        return valorTasa;
    }

    public void setValorTasa(double valorTasa) {
        this.valorTasa = valorTasa;
    }

    public double getCuotaFija() {
        return cuotaFija;
    }

    public void setCuotaFija(double cuotaFija) {
        this.cuotaFija = cuotaFija;
    }

    public Boolean getBonoAplica() {
        return bonoAplica;
    }

    public void setBonoAplica(Boolean bonoAplica) {
        this.bonoAplica = bonoAplica;
    }

    public String getBonoTipo() {
        return bonoTipo;
    }

    public void setBonoTipo(String bonoTipo) {
        this.bonoTipo = bonoTipo;
    }

    public double getBonoMonto() {
        return bonoMonto;
    }

    public void setBonoMonto(double bonoMonto) {
        this.bonoMonto = bonoMonto;
    }

    public Bonos_reglas getBono_Reglas_reglas_id() {
        return bono_Reglas_reglas_id;
    }

    public void setBono_Reglas_reglas_id(Bonos_reglas bono_Reglas_reglas_id) {
        this.bono_Reglas_reglas_id = bono_Reglas_reglas_id;
    }

    public List<Costes_adicionales> getCostos() {
        return costos;
    }

    public void setCostos(List<Costes_adicionales> costos) {
        this.costos = costos;
    }
}
