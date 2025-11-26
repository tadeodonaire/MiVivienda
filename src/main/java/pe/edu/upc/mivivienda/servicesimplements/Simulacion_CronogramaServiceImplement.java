package pe.edu.upc.mivivienda.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.mivivienda.entities.Simulacion_Cronograma;
import pe.edu.upc.mivivienda.repositories.Simulacion_CronogramaRepository;
import pe.edu.upc.mivivienda.servicesinterfaces.Simulacion_CronogramaService;

import java.util.List;

@Service
public class Simulacion_CronogramaServiceImplement implements Simulacion_CronogramaService {
    @Autowired
    private Simulacion_CronogramaRepository scR;

    @Override
    public void insert(Simulacion_Cronograma simulacion_cronograma) {
        scR.save(simulacion_cronograma);
    }

    @Override
    public List<Simulacion_Cronograma> list() {
        return scR.findAll();
    }

    @Override
    public void update(Simulacion_Cronograma simulacion_cronograma) {
        scR.save(simulacion_cronograma);
    }

    @Override
    public void delete(int id) {
        scR.deleteById(id);
    }

    @Override
    public Simulacion_Cronograma listarId(int id) {
        return scR.findById(id).orElse(new Simulacion_Cronograma());
    }
}
