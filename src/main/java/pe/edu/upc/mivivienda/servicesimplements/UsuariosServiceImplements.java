package pe.edu.upc.mivivienda.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upc.mivivienda.entities.Usuarios;
import pe.edu.upc.mivivienda.repositories.IUsuariosRepository;
import pe.edu.upc.mivivienda.servicesinterfaces.IUsuariosService;

import java.util.List;

@Service
public class UsuariosServiceImplements implements IUsuariosService {

    @Autowired
    private IUsuariosRepository uR;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void insert(Usuarios usuarios) {
        usuarios.setPassword(passwordEncoder.encode(usuarios.getPassword()));
        uR.save(usuarios);
    }

    @Override
    public List<Usuarios> list() {
        return uR.findAll();
    }

    @Override
    public void update(Usuarios usuarios) {
        uR.save(usuarios);
    }

    @Override
    public void delete(int id) {
        uR.deleteById(id);
    }

    @Override
    public Usuarios listarId(int id) {
        return uR.findById(id).orElse(new Usuarios());
    }
}
