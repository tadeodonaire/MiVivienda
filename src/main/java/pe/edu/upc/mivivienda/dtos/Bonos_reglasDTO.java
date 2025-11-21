package pe.edu.upc.mivivienda.dtos;

public class Bonos_reglasDTO {
    private int bonoRegla_id;
    private String nombre;
    private String moneda;
    private double precioMin;
    private double precioMax;
    private double ingresoMax;
    private double monto;

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
