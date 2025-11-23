package pe.edu.upc.mivivienda.servicesinterfaces;

import pe.edu.upc.mivivienda.entities.Propiedades;

import java.util.List;

public interface IPropiedadesService {
    //Create
    public void insert(Propiedades propiedades);
    //Read
    public List<Propiedades> list();
    //Update
    public void update(Propiedades propiedades);
    //Delete
    public void delete(int id);
    //ListarId
    public Propiedades listarId(int id);
}
