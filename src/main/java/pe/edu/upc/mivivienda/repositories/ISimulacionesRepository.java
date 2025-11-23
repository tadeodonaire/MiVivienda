package pe.edu.upc.mivivienda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mivivienda.entities.Simulaciones;

@Repository
public interface ISimulacionesRepository extends JpaRepository<Simulaciones, Integer> {
}
