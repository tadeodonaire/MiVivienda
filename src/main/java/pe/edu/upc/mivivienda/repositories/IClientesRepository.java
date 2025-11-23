package pe.edu.upc.mivivienda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mivivienda.entities.Clientes;

@Repository
public interface IClientesRepository extends JpaRepository<Clientes, Integer> {
}
