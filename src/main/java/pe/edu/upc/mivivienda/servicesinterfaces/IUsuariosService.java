package pe.edu.upc.mivivienda.servicesinterfaces;

import pe.edu.upc.mivivienda.entities.Usuarios;

import java.util.List;

public interface IUsuariosService {
    //Create
    public void insert(Usuarios usuarios);
    //Read
    public List<Usuarios> list();
    //Update
    public void update(Usuarios usuarios);
    //Delete
    public void delete(int id);
    //ListarId
    public Usuarios listarId(int id);
}
