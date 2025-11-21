package pe.edu.upc.mivivienda.dtos;

public class Entidades_financierasDTO {
    private int entidadFinanciera_id;
    private String nombre;
    private double TEA;
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
