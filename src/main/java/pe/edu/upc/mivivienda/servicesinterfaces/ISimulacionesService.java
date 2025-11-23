package pe.edu.upc.mivivienda.servicesinterfaces;

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
}
