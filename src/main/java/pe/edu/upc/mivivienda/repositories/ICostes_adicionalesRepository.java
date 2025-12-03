package pe.edu.upc.mivivienda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mivivienda.entities.Costes_adicionales;

import java.util.List;

@Repository
public interface ICostes_adicionalesRepository extends JpaRepository<Costes_adicionales, Integer> {
    @Query("""
      select c
      from Costes_adicionales c
      where c.simulaciones_simulacion_id.simulacion_id = :simId
      order by c.costesAdicional_id
    """)
    List<Costes_adicionales> findAllBySimulacionId(@Param("simId") int simId);
}