package pe.edu.upc.mivivienda.dtos;

import java.util.List;

public record SimulacionRequest(
        int propiedadId,                 // FK a Propiedades
        int entidadFinancieraId,         // FK a Entidades_financieras
        String moneda,                   // "PEN" / "USD"
        double precioVenta,              // si difiere del de la propiedad
        double cuotaInicial,             // S/.
        int tiempoAnios,                 // p.ej. 15, 20
        int frecuenciaPago,              // 12 mensual, 6 bimestral, etc.
        int tipoAnio,                    // 360 o 365 (convención)
        String tipoGracia,               // "SIN_GRACIA" | "TOTAL" | "PARCIAL"
        Integer cantidadGracia,          // 0..N (en periodos)
        boolean aplicarBono,             // true/false
        String bonoTipo,                 // "BBP_TRADICIONAL" | "BBP_INTEGRAL" | "BBP_SOSTENIBLE"
        Double tasaEfectivaAnual,        // TEA elegida (si viene de la UI); si null la tomas de la entidad
        List<Costes_adicionalesDTO> costos   // opcional
) {
}
