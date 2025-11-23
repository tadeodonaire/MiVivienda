package pe.edu.upc.mivivienda.dtos;

import pe.edu.upc.mivivienda.entities.Usuarios;

public class PropiedadesDTO {
    private int inmueble_id;
    private String nombreInmueble;
    private boolean areaInmueble;
    private String direccionInmueble;
    private double precioInmueble;
    private Usuarios clientes_cliente_id;

    public int getInmueble_id() {
        return inmueble_id;
    }

    public void setInmueble_id(int inmueble_id) {
        this.inmueble_id = inmueble_id;
    }

    public String getNombreInmueble() {
        return nombreInmueble;
    }

    public void setNombreInmueble(String nombreInmueble) {
        this.nombreInmueble = nombreInmueble;
    }

    public boolean isAreaInmueble() {
        return areaInmueble;
    }

    public void setAreaInmueble(boolean areaInmueble) {
        this.areaInmueble = areaInmueble;
    }

    public String getDireccionInmueble() {
        return direccionInmueble;
    }

    public void setDireccionInmueble(String direccionInmueble) {
        this.direccionInmueble = direccionInmueble;
    }

    public double getPrecioInmueble() {
        return precioInmueble;
    }

    public void setPrecioInmueble(double precioInmueble) {
        this.precioInmueble = precioInmueble;
    }

    public Usuarios getClientes_cliente_id() {
        return clientes_cliente_id;
    }

    public void setClientes_cliente_id(Usuarios clientes_cliente_id) {
        this.clientes_cliente_id = clientes_cliente_id;
    }
}
