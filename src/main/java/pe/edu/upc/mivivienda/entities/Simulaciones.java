package pe.edu.upc.mivivienda.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Simulaciones")
public class Simulaciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int simulacion_id;

    // ----- Datos del negocio -----
    @Column(name = "precioVenta", nullable = false)
    private double precioVenta;

    @Column(name = "cuotaInicial", nullable = false)
    private double cuotaInicial;

    @Column(name = "montoPrestamo", nullable = false)
    private double montoPrestamo; // calculado = precioVenta - cuotaInicial - bonoMonto (si aplica)

    @Column(name = "moneda", nullable = false, length = 3)
    private String moneda; // PEN / USD

    @Column(name = "tiempoAnios", nullable = false)
    private Integer tiempoAnios; // años del crédito

    @Column(name = "frecuenciaPago", nullable = false)
    private Integer frecuenciaPago; // 12=mensual, 6=bimestral, etc.

    @Column(name = "tipoAnio", nullable = false)
    private Integer tipoAnio; // años 360 o 365

    // Gracia
    @Column(name = "tipoGracia", length = 50)
    private String tipoGracia; // "SIN_GRACIA" | "TOTAL" | "PARCIAL"

    @Column(name = "cantidadGracia")
    private Integer cantidadGracia;

    // Seguros (rate fraccional)
    @Column(name = "seguroDesgravamen", nullable = false)
    private double seguroDesgravamen;

    @Column(name = "seguroInmueble", nullable = false)
    private double seguroInmueble;

    // ----- Relaciones -----

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "Propiedades_inmueble_id", nullable = false)
    private Propiedades propiedades_inmueble_id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "Entidades_financieras_entidadFinanciera_id", nullable = false)
    private Entidades_financieras entidades_financieras_entidadFinanciera_id;

    // ----- Config de tasa (usarás TEA) -----
    @Column(name = "tipoTasa", nullable = false, length = 3)
    private String tipoTasa; // "TEA"

    @Column(name = "valorTasa", nullable = false)
    private double valorTasa; // TEA normalizada (fracción)

    @Column(name = "tasaDescuentoAnual")
    private Double tasaDescuentoAnual;

    @Column(name = "cuotaFija")
    private double cuotaFija; // calculada método francés, opcional guardar

    // ----- Bono (congelado) -----
    @Column(name = "bonoAplica")
    private Boolean bonoAplica;

    @Column(name = "bonoTipo", length = 10)
    private String bonoTipo;

    @Column(name = "bonoMonto")
    private double bonoMonto;

    // Regla con la que se evaluó
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Bono_Reglas_reglas_id")
    private Bonos_reglas bono_Reglas_reglas_id;

    // ----- Costos adicionales 1:N -----
    @OneToMany(mappedBy = "simulaciones_simulacion_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Costes_adicionales> costos = new ArrayList<>();

    public Simulaciones() {}

    public Simulaciones(int simulacion_id, double precioVenta, double cuotaInicial, double montoPrestamo, String moneda, Integer tiempoAnios, Integer frecuenciaPago, Integer tipoAnio, String tipoGracia, Integer cantidadGracia, double seguroDesgravamen, double seguroInmueble, Propiedades propiedades_inmueble_id, Entidades_financieras entidades_financieras_entidadFinanciera_id, String tipoTasa, double valorTasa, Double tasaDescuentoAnual, double cuotaFija, Boolean bonoAplica, String bonoTipo, double bonoMonto, Bonos_reglas bono_Reglas_reglas_id, List<Costes_adicionales> costos) {
        this.simulacion_id = simulacion_id;
        this.precioVenta = precioVenta;
        this.cuotaInicial = cuotaInicial;
        this.montoPrestamo = montoPrestamo;
        this.moneda = moneda;
        this.tiempoAnios = tiempoAnios;
        this.frecuenciaPago = frecuenciaPago;
        this.tipoAnio = tipoAnio;
        this.tipoGracia = tipoGracia;
        this.cantidadGracia = cantidadGracia;
        this.seguroDesgravamen = seguroDesgravamen;
        this.seguroInmueble = seguroInmueble;
        this.propiedades_inmueble_id = propiedades_inmueble_id;
        this.entidades_financieras_entidadFinanciera_id = entidades_financieras_entidadFinanciera_id;
        this.tipoTasa = tipoTasa;
        this.valorTasa = valorTasa;
        this.tasaDescuentoAnual = tasaDescuentoAnual;
        this.cuotaFija = cuotaFija;
        this.bonoAplica = bonoAplica;
        this.bonoTipo = bonoTipo;
        this.bonoMonto = bonoMonto;
        this.bono_Reglas_reglas_id = bono_Reglas_reglas_id;
        this.costos = costos;
    }

    public Double getTasaDescuentoAnual() {
        return tasaDescuentoAnual;
    }

    public void setTasaDescuentoAnual(Double tasaDescuentoAnual) {
        this.tasaDescuentoAnual = tasaDescuentoAnual;
    }

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
