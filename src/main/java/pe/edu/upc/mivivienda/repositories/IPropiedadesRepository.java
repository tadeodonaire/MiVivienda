package pe.edu.upc.mivivienda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mivivienda.entities.Propiedades;

@Repository
public interface IPropiedadesRepository extends JpaRepository<Propiedades, Integer> {
}
