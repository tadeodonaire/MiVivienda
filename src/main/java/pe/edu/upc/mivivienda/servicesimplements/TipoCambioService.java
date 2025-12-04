package pe.edu.upc.mivivienda.servicesimplements;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import pe.edu.upc.mivivienda.dtos.MonedaDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TipoCambioService {
    private static final String API_KEY = "f5bc53f664ded792672e439d"; // la que usaste en el otro proyecto
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY;

    public double obtenerTipoCambio(String monedaBase, String monedaTarget) {
        URI direccion = URI.create(BASE_URL + "/pair/" + monedaBase + "/" + monedaTarget);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .build();

        try {
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            MonedaDTO dto = new Gson().fromJson(response.body(), MonedaDTO.class);
            return dto.conversion_rate();  // 1 monedaBase = conversion_rate monedaTarget
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error obteniendo tipo de cambio " + monedaBase + " -> " + monedaTarget, e);
        }
    }
}
