package pe.edu.upc.mivivienda.servicesimplements;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.mivivienda.entities.Usuarios;
import pe.edu.upc.mivivienda.repositories.IUsuariosRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsuariosRepository uR;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // CORRECCIÓN: quitar paréntesis extra
        Usuarios usuario = uR.findByUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException(
                    String.format("Usuario no encontrado con el nombre de usuario: %s", username)
            );
        }

        // Como el usuario solo tiene un rol (ManyToOne Usuarios -> Roles),
        // lo convertimos en una lista con una sola autoridad.
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (usuario.getRoles_rol_id() != null) {
            // Usa el nombre del rol; si necesitas prefijo "ROLE_", agrégalo aquí.
            String nombreRol = usuario.getRoles_rol_id().getNombre(); // campo 'nombre' en Roles
            authorities.add(new SimpleGrantedAuthority(nombreRol));
            // Si requieres prefijo:
            // authorities.add(new SimpleGrantedAuthority("ROLE_" + nombreRol));
        }

        // Creamos y retornamos un objeto UserDetails con los datos de tu entidad Usuarios
        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.isActivo(), // mapear 'activo' como "habilitado"
                true,               // cuenta no expirada
                true,               // credenciales no expiradas
                true,               // cuenta no bloqueada
                authorities         // lista de roles (solo uno en tu modelo actual)
        );
    }
}
