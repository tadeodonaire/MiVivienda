package pe.edu.upc.mivivienda.dtos;

import pe.edu.upc.mivivienda.entities.Simulaciones;

public class Simulacion_CronogramaDTO {
    private int simulacionCronograma_id;
    private int periodo;
    private double saldoInicial;
    private double saldoInicialIndexado;
    private double interes;
    private double cuota;
    private double amortizacion;
    private double seguroDesgravamen;
    private double seguroInmueble;
    private double saldoFinal;
    private Simulaciones simulaciones_simulacion_id;

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
