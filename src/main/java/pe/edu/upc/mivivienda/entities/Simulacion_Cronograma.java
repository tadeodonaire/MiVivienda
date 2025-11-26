package pe.edu.upc.mivivienda.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Simulacion_Cronograma")
public class Simulacion_Cronograma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int simulacionCronograma_id;

    @Column(name = "periodo", nullable = false)
    private int periodo;

    @Column(name = "saldoInicial", nullable = false)
    private double saldoInicial;

    @Column(name = "saldoInicialIndexado")
    private double saldoInicialIndexado;

    @Column(name = "interes", nullable = false)
    private double interes;

    @Column(name = "cuota", nullable = false)
    private double cuota;

    @Column(name = "amortizacion", nullable = false)
    private double amortizacion;

    @Column(name = "seguroDesgravamen", nullable = false)
    private double seguroDesgravamen;

    @Column(name = "seguroInmueble", nullable = false)
    private double seguroInmueble;

    @Column(name = "saldoFinal", nullable = false)
    private double saldoFinal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Simulaciones_simulacion_id", nullable = false)
    private Simulaciones simulaciones_simulacion_id;

    public Simulacion_Cronograma() {
    }

    public Simulacion_Cronograma(int simulacionCronograma_id, int periodo, double saldoInicial, double saldoInicialIndexado, double interes, double cuota, double amortizacion, double seguroDesgravamen, double seguroInmueble, double saldoFinal, Simulaciones simulaciones_simulacion_id) {
        this.simulacionCronograma_id = simulacionCronograma_id;
        this.periodo = periodo;
        this.saldoInicial = saldoInicial;
        this.saldoInicialIndexado = saldoInicialIndexado;
        this.interes = interes;
        this.cuota = cuota;
        this.amortizacion = amortizacion;
        this.seguroDesgravamen = seguroDesgravamen;
        this.seguroInmueble = seguroInmueble;
        this.saldoFinal = saldoFinal;
        this.simulaciones_simulacion_id = simulaciones_simulacion_id;
    }

    public int getSimulacionCronograma_id() {
        return simulacionCronograma_id;
    }

    public void setSimulacionCronograma_id(int simulacionCronograma_id) {
        this.simulacionCronograma_id = simulacionCronograma_id;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public double getSaldoInicialIndexado() {
        return saldoInicialIndexado;
    }

    public void setSaldoInicialIndexado(double saldoInicialIndexado) {
        this.saldoInicialIndexado = saldoInicialIndexado;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public double getCuota() {
        return cuota;
    }

    public void setCuota(double cuota) {
        this.cuota = cuota;
    }

    public double getAmortizacion() {
        return amortizacion;
    }

    public void setAmortizacion(double amortizacion) {
        this.amortizacion = amortizacion;
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

    public double getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(double saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public Simulaciones getSimulaciones_simulacion_id() {
        return simulaciones_simulacion_id;
    }

    public void setSimulaciones_simulacion_id(Simulaciones simulaciones_simulacion_id) {
        this.simulaciones_simulacion_id = simulaciones_simulacion_id;
    }
}
