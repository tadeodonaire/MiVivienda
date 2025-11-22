package pe.edu.upc.mivivienda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mivivienda.entities.Costes_adicionales;

@Repository
public interface ICostes_adicionalesRepository extends JpaRepository<Costes_adicionales, Integer> {
}