package com.example.ftp.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class RemoteAdminClient {

    private static final String SERVER_URL = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        // ВИПРАВЛЕННЯ: Використовуємо try-with-resources
        // Java автоматично викличе scanner.close() при виході з цього блоку
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("==========================================");
            System.out.println("   REMOTE FTP ADMIN CLIENT (LAB 9)        ");
            System.out.println("==========================================");

            while (true) {
                System.out.println("\nОберiть дiю:");
                System.out.println("1. Отримати список користувачiв (JSON)");
                System.out.println("2. Переглянути логи з'єднань");
                System.out.println("3. Створити демо-користувача ('remote_user')");
                System.out.println("0. Вихiд");
                System.out.print("Ваш вибiр > ");

                // Перевірка на наявність наступного рядка, щоб уникнути помилок при примусовому
                // закритті
                if (!scanner.hasNextLine()) {
                    break;
                }

                String choice = scanner.nextLine();

                try {
                    switch (choice) {
                        case "1":
                            getUsers();
                            break;
                        case "2":
                            getLogs();
                            break;
                        case "3":
                            createDemoUser();
                            break;
                        case "0":
                            System.out.println("Завершення роботи клiєнта...");
                            return; // Тут scanner закриється автоматично
                        default:
                            System.out.println("Невiрний вибiр. Спробуйте ще раз.");
                    }
                } catch (Exception e) {
                    System.err.println("Помилка з'єднання: " + e.getMessage());
                    System.err.println("Переконайтеся, що Сервер (FtpManagerApplication) запущений!");
                }
            }
        } // Кінець блоку try - тут ресурс scanner звільняється
    }

    private static void getUsers() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/users"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("\n--- ВIДПОВIДЬ СЕРВЕРА (Користувачi) ---");
        System.out.println(response.body());
    }

    private static void getLogs() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/logs"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("\n--- ВIДПОВIДЬ СЕРВЕРА (Логи) ---");
        System.out.println(response.body());
    }

    private static void createDemoUser() throws IOException, InterruptedException {
        String jsonBody = "{"
                + "\"username\":\"remote_user\","
                + "\"password\":\"12345\","
                + "\"homeDirectory\":\"C:/ftp/remote_user\","
                + "\"canWrite\":true,"
                + "\"maxUploadRate\":50000,"
                + "\"maxDownloadRate\":0"
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("\n--- РЕЗУЛЬТАТ СТВОРЕННЯ ---");
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Body: " + response.body());
    }
}