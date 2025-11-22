package pe.edu.upc.mivivienda.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.mivivienda.entities.Costes_adicionales;
import pe.edu.upc.mivivienda.repositories.ICostes_adicionalesRepository;
import pe.edu.upc.mivivienda.servicesinterfaces.ICostes_adicionalesService;

import java.util.List;

@Service
public class Costes_adicionalesServiceImplement implements ICostes_adicionalesService {

    @Autowired
    private ICostes_adicionalesRepository caR;

    @Override
    public void insert(Costes_adicionales costes_adicionales) {
        caR.save(costes_adicionales);
    }

    @Override
    public List<Costes_adicionales> list() {
        return caR.findAll();
    }

    @Override
    public void update(Costes_adicionales costes_adicionales) {
        caR.save(costes_adicionales);
    }

    @Override
    public void delete(int id) {
        caR.deleteById(id);
    }

    @Override
    public Costes_adicionales listarId(int id) {
        return caR.findById(id).orElse(new Costes_adicionales());
    }
}