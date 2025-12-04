package pe.edu.upc.mivivienda.dtos;

import java.util.List;

public record SimulacionRequest(
        int propiedadId,
        int entidadFinancieraId,
        String moneda,
        double precioVenta,
        double cuotaInicial,
        int tiempoAnios,
        int frecuenciaPago,
        int tipoAnio,
        String tipoGracia,
        Integer cantidadGracia,
        Boolean aplicarBono,
        String bonoTipo,          // "BBI","BBP","TP", etc.
        Double tasaEfectivaAnual,
        List<Costes_adicionalesDTO> costos,
        Double tasaDescuentoAnual,
        Integer bonoReglaId
) {}

