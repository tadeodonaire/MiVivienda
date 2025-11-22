package pe.edu.upc.mivivienda.servicesinterfaces;

import pe.edu.upc.mivivienda.entities.Costes_adicionales;

import java.util.List;

public interface ICostes_adicionalesService {

    void insert(Costes_adicionales costes_adicionales);

    List<Costes_adicionales> list();

    void update(Costes_adicionales costes_adicionales);

    void delete(int id);

    Costes_adicionales listarId(int id);
}