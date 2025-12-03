package pe.edu.upc.mivivienda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.mivivienda.entities.Clientes;

import java.util.List;

@Repository
public interface IClientesRepository extends JpaRepository<Clientes, Integer> {

    @Query(value = "SELECT\n" +
            "  c.cliente_id,\n" +
            "  c.nombre,\n" +
            "  c.apellido,\n" +
            "  c.dni,\n" +
            "  c.ingresos_mensuales,\n" +
            "  c.moneda,\n" +
            "  COALESCE(\n" +
            "    json_agg(\n" +
            "      json_build_object(\n" +
            "        'simulacion_id', s.simulacion_id,\n" +
            "        'precio_venta', s.precio_venta,\n" +
            "        'cuota_inicial', s.cuota_inicial,\n" +
            "        'monto_prestamo', s.monto_prestamo,\n" +
            "        'tiempo_anios', s.tiempo_anios,\n" +
            "        'frecuencia_pago', s.frecuencia_pago,\n" +
            "        'tipo_anio', s.tipo_anio,\n" +
            "        'tipo_gracia', s.tipo_gracia,\n" +
            "        'cantidad_gracia', s.cantidad_gracia,\n" +
            "        'seguro_desgravamen', s.seguro_desgravamen,\n" +
            "        'seguro_inmueble', s.seguro_inmueble,\n" +
            "        'tipo_tasa', s.tipo_tasa,\n" +
            "        'valor_tasa', s.valor_tasa,\n" +
            "        'cuota_fija', s.cuota_fija,\n" +
            "        'bono_aplica', s.bono_aplica,\n" +
            "        'bono_tipo', s.bono_tipo,\n" +
            "        'bono_monto', s.bono_monto,\n" +
            "        'inmueble_id', p.inmueble_id,\n" +
            "        'nombre_inmueble', p.nombre_inmueble,\n" +
            "        'precio_inmueble', p.precio_inmueble\n" +
            "      )\n" +
            "    ) FILTER (WHERE s.simulacion_id IS NOT NULL),\n" +
            "    '[]'::json\n" +
            "  ) AS simulaciones\n" +
            "FROM clientes c\n" +
            "LEFT JOIN propiedades p ON p.clientes_cliente_id = c.cliente_id\n" +
            "LEFT JOIN simulaciones s ON s.propiedades_inmueble_id = p.inmueble_id\n" +
            "GROUP BY c.cliente_id, c.nombre, c.apellido, c.ingresos_mensuales, c.moneda\n" +
            "ORDER BY c.apellido, c.nombre\n", nativeQuery = true)
    public List<String[]> getVerSimulaciones();
}
