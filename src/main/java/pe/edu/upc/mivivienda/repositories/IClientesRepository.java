package pe.edu.upc.mivivienda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mivivienda.entities.Clientes;

import java.util.List;
import java.util.Optional;

@Repository
public interface IClientesRepository extends JpaRepository<Clientes, Integer> {
    // Busca por username del dueño (tabla usuarios)
    @Query(value = """
            SELECT c.*
            FROM clientes c
            JOIN usuarios u ON u.usuario_id = c.usuarios_usuario_id
            WHERE UPPER(u.username) = UPPER(:username)
            """, nativeQuery = true)
    List<Clientes> findAllByUsuarioUsernameNative(@Param("username") String username);

    // Alternativa por userId (si lo prefieres)
    @Query(value = """
            SELECT c.*
            FROM clientes c
            WHERE c.usuarios_usuario_id = :userId
            """, nativeQuery = true)
    List<Clientes> findAllByUsuarioIdNative(@Param("userId") int userId);
}