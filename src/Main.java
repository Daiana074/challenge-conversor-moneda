import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static double convertir(double cantidadOrigen, double tasaOrigen, double tasaDestino) {
        // tasas están expresadas como: 1 USD = tasaX (unidades de X por 1 USD)
        // Para convertir: primero pasar a USD, luego a moneda destino:
        // cantidadUSD = cantidadOrigen / tasaOrigen
        // cantidadDestino = cantidadUSD * tasaDestino
        return cantidadOrigen * (tasaDestino / tasaOrigen);
    }

    public static void mostrarMenuPrincipal() {
        System.out.println("\n=== Bienvenido/a al Conversor de Moneda ===");
        System.out.println("1. Realizar conversión");
        System.out.println("2. Mostrar historial de conversiones");
        System.out.println("3. Salir");
        System.out.print("Elija una opción válida: ");
    }

    public static void mostrarMonedas(String[] monedas) {
        System.out.println("\nElija la moneda:");
        for (int i = 0; i < monedas.length; i++) {
            System.out.println((i + 1) + ". " + monedas[i]);
        }
        System.out.print("Ingrese el número de la moneda: ");
    }

    public static void main(String[] args) {

        // Lista para historial
        List<String> historial = new ArrayList<>();

        // PASO 4: Creando cliente HTTP
        HttpClient cliente = HttpClient.newHttpClient();

        // PASO 5: URL y solicitud (base USD)
        String url = "https://api.exchangerate-api.com/v4/latest/USD";
        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // PASO 6 y 7: Enviar solicitud y analizar JSON
        try {
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            JsonObject datosJson = new Gson().fromJson(respuesta.body(), JsonObject.class);
            JsonObject rates = datosJson.getAsJsonObject("rates");

            String[] monedasFiltradas = {"USD", "ARS", "BOB", "BRL", "EUR", "CLP", "COP"};
            Scanner scanner = new Scanner(System.in);
            boolean continuar = true;

            while (continuar) {
                mostrarMenuPrincipal();
                int opcionPrincipal;
                // validar int
                if (scanner.hasNextInt()) {
                    opcionPrincipal = scanner.nextInt();
                } else {
                    System.out.println("Entrada inválida. Intente nuevamente.");
                    scanner.nextLine(); // limpiar
                    continue;
                }

                if (opcionPrincipal == 1) {
                    // Elegir moneda de origen
                    mostrarMonedas(monedasFiltradas);
                    int idxOrigen;
                    if (scanner.hasNextInt()) {
                        idxOrigen = scanner.nextInt();
                    } else {
                        System.out.println("Entrada inválida. Volviendo al menú principal.");
                        scanner.nextLine();
                        continue;
                    }
                    if (idxOrigen < 1 || idxOrigen > monedasFiltradas.length) {
                        System.out.println("Opción de moneda inválida. Volviendo al menú principal.");
                        continue;
                    }
                    String monedaOrigen = monedasFiltradas[idxOrigen - 1];

                    // Elegir moneda de destino
                    mostrarMonedas(monedasFiltradas);
                    int idxDestino;
                    if (scanner.hasNextInt()) {
                        idxDestino = scanner.nextInt();
                    } else {
                        System.out.println("Entrada inválida. Volviendo al menú principal.");
                        scanner.nextLine();
                        continue;
                    }
                    if (idxDestino < 1 || idxDestino > monedasFiltradas.length) {
                        System.out.println("Opción de moneda inválida. Volviendo al menú principal.");
                        continue;
                    }
                    String monedaDestino = monedasFiltradas[idxDestino - 1];

                    // Pedir cantidad en moneda de origen
                    System.out.print("Ingrese la cantidad en " + monedaOrigen + " a convertir: ");
                    double cantidadOrigen;
                    if (scanner.hasNextDouble()) {
                        cantidadOrigen = scanner.nextDouble();
                    } else {
                        System.out.println("Cantidad inválida. Volviendo al menú principal.");
                        scanner.nextLine();
                        continue;
                    }

                    // Obtener tasas desde el JSON
                    if (!rates.has(monedaOrigen)) {
                        System.out.println("Tasa para la moneda de origen (" + monedaOrigen + ") no disponible.");
                        continue;
                    }
                    if (!rates.has(monedaDestino)) {
                        System.out.println("Tasa para la moneda destino (" + monedaDestino + ") no disponible.");
                        continue;
                    }

                    double tasaOrigen = rates.get(monedaOrigen).getAsDouble();
                    double tasaDestino = rates.get(monedaDestino).getAsDouble();

                    double resultado = convertir(cantidadOrigen, tasaOrigen, tasaDestino);

                    // Marca de tiempo
                    LocalDateTime ahora = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    String registro = "[" + ahora.format(formatter) + "] " +
                            cantidadOrigen + " " + monedaOrigen + " -> " + resultado + " " + monedaDestino;
                    historial.add(registro);

                    System.out.println(String.format("%.4f %s = %.4f %s", cantidadOrigen, monedaOrigen, resultado, monedaDestino));

                } else if (opcionPrincipal == 2) {
                    System.out.println("\n=== Historial de conversiones ===");
                    if (historial.isEmpty()) {
                        System.out.println("No hay conversiones realizadas aún.");
                    } else {
                        for (String h : historial) {
                            System.out.println(h);
                        }
                    }
                } else if (opcionPrincipal == 3) {
                    System.out.println("Saliendo del conversor. ¡Hasta luego!");
                    continuar = false;
                } else {
                    System.out.println("Opción inválida. Intente nuevamente.");
                }
                // limpiar buffer de línea antes de la siguiente iteración
                scanner.nextLine();
            }

        } catch (Exception e) {
            System.out.println("Error al enviar la solicitud o procesar JSON: " + e.getMessage());
        }
    }
}
