package pe.edu.upc.mivivienda.servicesinterfaces;

import pe.edu.upc.mivivienda.entities.Bonos_reglas;

import java.util.List;

public interface IBonos_reglasService {
    //Create
    public void insert(Bonos_reglas bonos_reglas);
    //Read
    public List<Bonos_reglas> list();
    //Update
    public void update(Bonos_reglas bonos_reglas);
    //Delete
    public void delete(int id);
    //ListarId
    public Bonos_reglas listarId(int id);
}
