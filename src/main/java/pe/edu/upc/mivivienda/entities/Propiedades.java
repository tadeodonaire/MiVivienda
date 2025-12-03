package pe.edu.upc.mivivienda.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Propiedades")
public class Propiedades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inmueble_id;

    @Column(name = "nombreInmueble", nullable = false, length = 100)
    private String nombreInmueble;

    @Column(name = "areaInmueble", nullable = false)
    private double areaInmueble;

    @Column(name = "direccionInmueble", nullable = false, length = 100)
    private String direccionInmueble;

    @Column(name = "precioInmueble", nullable = false)
    private double precioInmueble;

    @ManyToOne
    @JoinColumn(name = "clientes_cliente_id",nullable = false)
    private Clientes clientes_cliente_id;

    public Propiedades() {
    }

    public Propiedades(int inmueble_id, String nombreInmueble, double areaInmueble, String direccionInmueble, double precioInmueble, Clientes clientes_cliente_id) {
        this.inmueble_id = inmueble_id;
        this.nombreInmueble = nombreInmueble;
        this.areaInmueble = areaInmueble;
        this.direccionInmueble = direccionInmueble;
        this.precioInmueble = precioInmueble;
        this.clientes_cliente_id = clientes_cliente_id;
    }

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

    public double isAreaInmueble() {
        return areaInmueble;
    }

    public void setAreaInmueble(double areaInmueble) {
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

    public Clientes getClientes_cliente_id() {
        return clientes_cliente_id;
    }

    public void setClientes_cliente_id(Clientes clientes_cliente_id) {
        this.clientes_cliente_id = clientes_cliente_id;
    }
}
