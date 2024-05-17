package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import com.google.gson.Gson;

public class CurrencyConvert {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/69d9d065e9eef70beecea626/latest/USD";
    private static Map<String, Double> rates;

    public static void main(String[] args) {
        // Obtener las tasas de conversión desde la API
        fetchConversionRates();

        // Si las tasas de conversión se obtuvieron correctamente, mostrar el menú
        if (rates != null) {
            showMenu();
        } else {
            System.out.println("No se pudo obtener las tasas de conversión. Por favor, intente más tarde.");
        }
    }

    // Método para obtener las tasas de conversión desde la API
    private static void fetchConversionRates() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                Gson gson = new Gson();
                ExchangeRateResponse exchangeRateResponse = gson.fromJson(responseBody, ExchangeRateResponse.class);
                rates = exchangeRateResponse.getRates();
            } else {
                System.out.println("Error al obtener las tasas de conversión: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para mostrar el menú y manejar la interacción con el usuario
    private static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n--- Conversor de Monedas ---");
            System.out.println("1. Convertir moneda");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");
            int option = scanner.nextInt();
            scanner.nextLine();  // Consumir el salto de línea

            switch (option) {
                case 1:
                    handleConversion(scanner);
                    break;
                case 2:
                    exit = true;
                    System.out.println("Saliendo del programa. ¡Adiós!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        }
        scanner.close();
    }

    // Método para manejar la conversión de moneda
    private static void handleConversion(Scanner scanner) {
        System.out.print("Ingrese la cantidad a convertir: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // Consumir el salto de línea

        System.out.print("Ingrese la moneda de origen (código): ");
        String fromCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese la moneda de destino (código): ");
        String toCurrency = scanner.nextLine().toUpperCase();

        try {
            double convertedAmount = convertCurrency(amount, fromCurrency, toCurrency);
            System.out.printf("\n%.2f %s es igual a %.2f %s\n", amount, fromCurrency, convertedAmount, toCurrency);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // Método para convertir monedas
    private static double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        if (!rates.containsKey(fromCurrency) || !rates.containsKey(toCurrency)) {
            throw new IllegalArgumentException("Moneda no soportada.");
        }

        double fromRate = rates.get(fromCurrency);
        double toRate = rates.get(toCurrency);
        return (amount / fromRate) * toRate;
    }
}





