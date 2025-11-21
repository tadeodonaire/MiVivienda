package pe.edu.upc.mivivienda.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.mivivienda.entities.Entidades_financieras;
import pe.edu.upc.mivivienda.repositories.IEntidades_financierasRepository;
import pe.edu.upc.mivivienda.servicesinterfaces.IEntidades_financierasService;

import java.util.List;

@Service
public class Entidades_financierasServiceImplement implements IEntidades_financierasService {
    @Autowired
    private IEntidades_financierasRepository efR;


    @Override
    public void insert(Entidades_financieras entidades_financieras) {
        efR.save(entidades_financieras);
    }

    @Override
    public List<Entidades_financieras> list() {
        return efR.findAll();
    }

    @Override
    public void update(Entidades_financieras entidades_financieras) {
        efR.save(entidades_financieras);
    }

    @Override
    public void delete(int id) {
        efR.deleteById(id);
    }

    @Override
    public Entidades_financieras listarId(int id) {
        return efR.findById(id).orElse(new Entidades_financieras());
    }
}
