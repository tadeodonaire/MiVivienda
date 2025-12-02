package pe.edu.upc.mivivienda.servicesinterfaces;

import org.springframework.data.repository.query.Param;
import pe.edu.upc.mivivienda.entities.Clientes;

import java.util.List;

public interface IClientesService {
    //Create
    public void insert(Clientes clientes);
    //Read
    public List<Clientes> list();
    //Update
    public void update(Clientes clientes);
    //Delete
    public void delete(int id);
    //ListarId
    public Clientes listarId(int id);

    public List<Clientes> listarPorUsername(String username);
    public List<Clientes> listarPorUserId(int userId);
}
