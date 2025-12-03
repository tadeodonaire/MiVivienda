package pe.edu.upc.mivivienda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mivivienda.entities.Propiedades;

import java.util.List;

@Repository
public interface IPropiedadesRepository extends JpaRepository<Propiedades, Integer> {
    @Query("""
         select p
         from Propiedades p
         where p.clientes_cliente_id.cliente_id = :clienteId
         """)
    List<Propiedades> findByCliente(@Param("clienteId") int clienteId);

    // Propiedades que pertenecen a clientes del consultor (sin indicar cliente)
    @Query("""
         select p
         from Propiedades p
         where p.clientes_cliente_id.usuarios_usuario_id.usuario_id = :usuarioId
         """)
    List<Propiedades> findForConsultor(@Param("usuarioId") int usuarioId);
}
