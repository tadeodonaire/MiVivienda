package pe.edu.upc.mivivienda.servicesimplements;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.mivivienda.entities.Clientes;
import pe.edu.upc.mivivienda.repositories.IClientesRepository;
import pe.edu.upc.mivivienda.servicesinterfaces.IClientesService;

import java.util.List;

@Service
public class ClientesServicesImplement implements IClientesService {

    @Autowired
    private IClientesRepository cR;

    @Override
    public void insert(Clientes clientes) {
        cR.save(clientes);
    }

    @Override
    public List<Clientes> list() {
        return cR.findAll();
    }

    @Override
    public void update(Clientes clientes) {
        cR.save(clientes);
    }

    @Override
    public void delete(int id) {
        cR.deleteById(id);
    }

    @Override
    public Clientes listarId(int id) {
        return cR.findById(id).orElse(new Clientes());
    }
}
