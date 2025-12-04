package pe.edu.upc.mivivienda.dtos;

public record MonedaDTO(String base_code,
                        String target_code,
                        double conversion_rate) {
}
