package pe.edu.upc.mivivienda.servicesinterfaces;


import pe.edu.upc.mivivienda.entities.Simulacion_Cronograma;

import java.util.List;

public interface Simulacion_CronogramaService {
    //Create
    public void insert(Simulacion_Cronograma simulacion_cronograma);
    //Read
    public List<Simulacion_Cronograma> list();
    //Update
    public void update(Simulacion_Cronograma simulacion_cronograma);
    //Delete
    public void delete(int id);
    //ListarId
    public Simulacion_Cronograma listarId(int id);
}
