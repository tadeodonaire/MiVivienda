package pe.edu.upc.mivivienda.servicesinterfaces;

import pe.edu.upc.mivivienda.dtos.SimulacionRequest;
import pe.edu.upc.mivivienda.dtos.SimulacionConCronogramaResponse;
import pe.edu.upc.mivivienda.entities.Bonos_reglas;
import pe.edu.upc.mivivienda.entities.Simulaciones;

import java.util.List;

public interface ISimulacionesService {
    //Create
    public void insert(Simulaciones simulaciones);
    //Read
    public List<Simulaciones> list();
    //Update
    public void update(Simulaciones simulaciones);
    //Delete
    public void delete(int id);
    //ListarId
    public Simulaciones listarId(int id);
    // Simulaciones por usuario
    SimulacionConCronogramaResponse crearConCronograma(SimulacionRequest req);
    SimulacionConCronogramaResponse recalcularHoja(int id);
    List<Bonos_reglas> evaluarTechoPropio(int propiedadId, String moneda);
}
