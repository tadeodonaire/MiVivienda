package pe.edu.upc.mivivienda.dtos;

import pe.edu.upc.mivivienda.entities.Roles;

import java.time.LocalDate;

public class UsuariosDTO {
    private int usuario_id;
    private String username;
    private String password;
    private boolean activo;
    private LocalDate creado_en;
    private Roles roles_rol_id;

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
