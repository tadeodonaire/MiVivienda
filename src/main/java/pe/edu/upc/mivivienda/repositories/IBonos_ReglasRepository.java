package pe.edu.upc.mivivienda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mivivienda.entities.Bonos_reglas;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBonos_ReglasRepository extends JpaRepository<Bonos_reglas, Integer> {
    @Query("""
    SELECT b FROM Bonos_reglas b
     WHERE UPPER(b.nombre) = UPPER(:nombre)
       AND UPPER(b.moneda) = UPPER(:moneda)
       AND :precio >= b.precioMin
       AND (:precio < b.precioMax OR b.precioMax IS NULL)
     ORDER BY b.precioMin DESC""")

    List<Bonos_reglas> findBands(@Param("nombre") String nombre,
                                 @Param("moneda") String moneda,
                                 @Param("precio") double precio);

    @Query("""
       SELECT b
       FROM Bonos_reglas b
       WHERE b.moneda = :moneda
         AND :precio BETWEEN b.precioMin AND b.precioMax
         AND :ingreso <= b.ingresoMax
         AND b.nombre LIKE 'TP-%'
       ORDER BY b.monto DESC
       """)
    List<Bonos_reglas> findTechoPropioElegibles(
            @Param("moneda") String moneda,
            @Param("precio") double precioInmueble,
            @Param("ingreso") double ingresoMensualHogar);
}
