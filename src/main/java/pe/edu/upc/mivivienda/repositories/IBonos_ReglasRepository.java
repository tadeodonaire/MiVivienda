package pe.edu.upc.mivivienda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mivivienda.entities.Bonos_reglas;

@Repository
public interface IBonos_ReglasRepository extends JpaRepository<Bonos_reglas, Integer> {
}
