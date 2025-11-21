package pe.edu.upc.mivivienda.servicesinterfaces;

import pe.edu.upc.mivivienda.entities.Roles;

import java.util.List;

public interface IRolesService {
    //Create
    public void insert(Roles roles);
    //Read
    public List<Roles> list();
    //Update
    public void update(Roles roles);
    //Delete
    public void delete(int id);
    //ListarId
    public Roles listarId(int id);
}
