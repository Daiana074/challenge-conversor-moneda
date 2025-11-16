import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) {

        // Creando cliente HTTP (PASO 4)
        HttpClient cliente = HttpClient.newHttpClient();

        System.out.println("Cliente HTTP creado correctamente.");

        // Nada más por ahora
    }
}
