package pe.edu.upc.mivivienda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mivivienda.entities.Simulacion_Cronograma;

@Repository
public interface ISimulacion_CronogramaRepository extends JpaRepository<Simulacion_Cronograma, Integer> {
}
