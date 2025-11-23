package pe.edu.upc.mivivienda.dtos;


import pe.edu.upc.mivivienda.entities.Usuarios;

public class ClientesDTO {
    private int cliente_id;
    private String nombre;
    private String apellido;
    private String correo;
    private int dni;
    private double ingresosMensuales;
    private String moneda;
    private Usuarios usuarios_usuario_id;

    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public double getIngresosMensuales() {
        return ingresosMensuales;
    }

    public void setIngresosMensuales(double ingresosMensuales) {
        this.ingresosMensuales = ingresosMensuales;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Usuarios getUsuarios_usuario_id() {
        return usuarios_usuario_id;
    }

    public void setUsuarios_usuario_id(Usuarios usuarios_usuario_id) {
        this.usuarios_usuario_id = usuarios_usuario_id;
    }
}
