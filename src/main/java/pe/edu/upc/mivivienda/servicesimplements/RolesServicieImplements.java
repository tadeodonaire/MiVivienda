package pe.edu.upc.mivivienda.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.mivivienda.entities.Roles;
import pe.edu.upc.mivivienda.repositories.IRolesRepository;
import pe.edu.upc.mivivienda.servicesinterfaces.IRolesService;

import java.util.List;

@Service
public class RolesServicieImplements implements IRolesService {
    @Autowired
    private IRolesRepository rR;


    @Override
    public void insert(Roles roles) {
        rR.save(roles);
    }

    @Override
    public List<Roles> list() {
        return rR.findAll();
    }

    @Override
    public void update(Roles roles) {
        rR.save(roles);
    }

    @Override
    public void delete(int id) {
        rR.deleteById(id);
    }

    @Override
    public Roles listarId(int id) {
        return rR.findById(id).orElse(new Roles());
    }
}
