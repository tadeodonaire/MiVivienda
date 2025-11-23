package pe.edu.upc.mivivienda.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Usuarios")
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int usuario_id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    @Column(name = "creado_en", nullable = false)
    private LocalDate creado_en;

    @ManyToOne
    @JoinColumn(name = "roles_rol_id",nullable = false)
    private Roles roles_rol_id;

    public Usuarios() {
    }

    public Usuarios(int usuario_id, String username, String password, boolean activo, LocalDate creado_en, Roles roles_rol_id) {
        this.usuario_id = usuario_id;
        this.username = username;
        this.password = password;
        this.activo = activo;
        this.creado_en = creado_en;
        this.roles_rol_id = roles_rol_id;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDate getCreado_en() {
        return creado_en;
    }

    public void setCreado_en(LocalDate creado_en) {
        this.creado_en = creado_en;
    }

    public Roles getRoles_rol_id() {
        return roles_rol_id;
    }

    public void setRoles_rol_id(Roles roles_rol_id) {
        this.roles_rol_id = roles_rol_id;
    }
}
