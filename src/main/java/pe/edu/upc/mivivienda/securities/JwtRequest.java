package pe.edu.upc.mivivienda.securities;

import java.io.Serializable;

public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String password;

    // Constructor vacío (obligatorio para serialización)
    public JwtRequest() {
        super();
    }

    // Constructor con parámetros
    public JwtRequest(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    // Getters y Setters
    public static long getSerialversionuid() {
        return serialVersionUID;
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
}
