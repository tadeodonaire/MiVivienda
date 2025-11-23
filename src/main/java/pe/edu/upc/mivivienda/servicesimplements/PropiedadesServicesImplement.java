package pe.edu.upc.mivivienda.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.mivivienda.entities.Propiedades;
import pe.edu.upc.mivivienda.repositories.IPropiedadesRepository;
import pe.edu.upc.mivivienda.servicesinterfaces.IPropiedadesService;

import java.util.List;

@Service
public class PropiedadesServicesImplement implements IPropiedadesService {

    @Autowired
    private IPropiedadesRepository pR;

    @Override
    public void insert(Propiedades propiedades) {
        pR.save(propiedades);
    }

    @Override
    public List<Propiedades> list() {
        return pR.findAll();
    }

    @Override
    public void update(Propiedades propiedades) {
        pR.save(propiedades);
    }

    @Override
    public void delete(int id) {
        pR.deleteById(id);
    }

    @Override
    public Propiedades listarId(int id) {
        return pR.findById(id).orElse(new Propiedades());
    }
}
