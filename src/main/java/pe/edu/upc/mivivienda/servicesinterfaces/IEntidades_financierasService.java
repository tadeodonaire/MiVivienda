package pe.edu.upc.mivivienda.servicesinterfaces;

import pe.edu.upc.mivivienda.entities.Entidades_financieras;

import java.util.List;

public interface IEntidades_financierasService {
    //Create
    public void insert(Entidades_financieras entidades_financieras);
    //Read
    public List<Entidades_financieras> list();
    //Update
    public void update(Entidades_financieras entidades_financieras);
    //Delete
    public void delete(int id);
    //ListarId
    public Entidades_financieras listarId(int id);
}
