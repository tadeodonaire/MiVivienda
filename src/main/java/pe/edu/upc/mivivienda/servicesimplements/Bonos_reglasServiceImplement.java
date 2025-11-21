package pe.edu.upc.mivivienda.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.mivivienda.entities.Bonos_reglas;
import pe.edu.upc.mivivienda.repositories.IBonos_ReglasRepository;
import pe.edu.upc.mivivienda.servicesinterfaces.IBonos_reglasService;

import java.util.List;

@Service
public class Bonos_reglasServiceImplement implements IBonos_reglasService {
    @Autowired
    private IBonos_ReglasRepository brR;

    @Override
    public void insert(Bonos_reglas bonos_reglas) {
        brR.save(bonos_reglas);
    }

    @Override
    public List<Bonos_reglas> list() {
        return brR.findAll();
    }

    @Override
    public void update(Bonos_reglas bonos_reglas) {
        brR.save(bonos_reglas);
    }

    @Override
    public void delete(int id) {
        brR.deleteById(id);
    }

    @Override
    public Bonos_reglas listarId(int id) {
        return brR.findById(id).orElse(new Bonos_reglas());
    }
}
