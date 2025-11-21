package pe.edu.upc.mivivienda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mivivienda.entities.Entidades_financieras;

@Repository
public interface IEntidades_financierasRepository extends JpaRepository<Entidades_financieras, Integer> {
}
