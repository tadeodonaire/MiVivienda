package pe.edu.upc.mivivienda.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.mivivienda.entities.Simulaciones;
import pe.edu.upc.mivivienda.repositories.ISimulacionesRepository;
import pe.edu.upc.mivivienda.servicesinterfaces.ISimulacionesService;

import java.util.List;

@Service
public class SimulacionesServiceImplement implements ISimulacionesService {
    @Autowired
    private ISimulacionesRepository sR;

    @Override
    public void insert(Simulaciones simulaciones) {
        sR.save(simulaciones);
    }

    @Override
    public List<Simulaciones> list() {
        return sR.findAll();
    }

    @Override
    public void update(Simulaciones simulaciones) {
        sR.save(simulaciones);
    }

    @Override
    public Simulaciones listarId(int id) {
        return sR.findById(id).orElse(new  Simulaciones());
    }

    @Override
    public void delete(int id) {
        sR.deleteById(id);
    }
}
