package pe.edu.upc.mivivienda.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Clientes")
public class Clientes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cliente_id;

    @Column(name = "nombre",nullable = false,length = 25)
    private String nombre;

    @Column(name = "apellido",nullable = false,length = 25)
    private String apellido;

    @Column(name = "correo",nullable = false,length = 50)
    private String correo;

    @Column(name = "dni",nullable = false,length = 9)
    private int dni;

    @Column(name = "ingresosMensuales",nullable = false)
    private double ingresosMensuales;

    @Column(name = "moneda",nullable = false,length = 3)
    private String moneda;

    @ManyToOne
    @JoinColumn(name = "usuarios_usuario_id",nullable = false)
    private Usuarios usuarios_usuario_id;

    public Clientes() {
    }

    public Clientes(int cliente_id, String nombre, String apellido, String correo, int dni, double ingresosMensuales, String moneda, Usuarios usuarios_usuario_id) {
        this.cliente_id = cliente_id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.dni = dni;
        this.ingresosMensuales = ingresosMensuales;
        this.moneda = moneda;
        this.usuarios_usuario_id = usuarios_usuario_id;
    }

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
