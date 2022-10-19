package ru.yandex.practicum.filmorate.controller;

import com.google.gson.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.LocalDateAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    static Gson gson;

    @BeforeAll
    static void setUp() {
        SpringApplication.run(FilmorateApplication.class);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @Test
    @DisplayName("filmControllerValidationTest1-save-normal operation")
    void filmControllerValidationTest1() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n  \"name\": \"Матрица\",\n  \"description\": \"adipisicing\",\n  \"releaseDate\": \"2020-12-12\",\n  \"duration\": 100\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Film film = gson.fromJson(response.body(), Film.class);
        assertEquals(200, response.statusCode());
        assertEquals("Матрица", film.getName());
    }

    @Test
    @DisplayName("filmControllerValidationTest2-save-name is empty")
    void filmControllerValidationTest2() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n  \"name\": \"\",\n  \"description\": \"adipisicing\",\n  \"releaseDate\": \"2020-12-12\",\n  \"duration\": 100\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject message = jsonElement.getAsJsonObject();

        assertEquals(400, response.statusCode());
        assertEquals("Name is empty!", message.get("message").getAsString());
    }

    @Test
    @DisplayName("filmControllerValidationTest3-save-description is 200")
    void filmControllerValidationTest3() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n  \"name\": \"Матрица\",\n  \"description\": \"Li Europan lingues es membres del sam familie. " +
                "Lor separat existentie es un myth. Por scientie, musica, sport etc., li tot Europa usa li sam vocabular" +
                "ium. Li lingues differe solmen in li grammatica, l\",\n  \"releaseDate\": \"2020-12-12\",\n  \"duration\": 100\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Film film = gson.fromJson(response.body(), Film.class);

        assertEquals(200, response.statusCode());
        assertEquals(200, film.getDescription().length());
    }

    @Test
    @DisplayName("filmControllerValidationTest4-save-description is over 200")
    void filmControllerValidationTest4() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n  \"name\": \"Матрица\",\n  \"description\": \"Li Europan lingues es membres del sam familie. " +
                "Lor separat existentie es un myth. Por scientie, musica, sport etc., li tot Europa usa li sam vocabular" +
                "ium. Li lingues differe solmen in li grammatica, l+\",\n  \"releaseDate\": \"2020-12-12\",\n  \"duration\": 100\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject message = jsonElement.getAsJsonObject();

        assertEquals(400, response.statusCode());
        assertEquals("Description length extends 200 chars!", message.get("message").getAsString());
    }

    @Test
    @DisplayName("filmControllerValidationTest5-save-realiseDate is 28 DEC 1895")
    void filmControllerValidationTest5() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n  \"name\": \"Матрица\",\n  \"description\": \"adipisicing\",\n  \"releaseDate\": \"1895-12-28\",\n  \"duration\": 100\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Film film = gson.fromJson(response.body(), Film.class);

        assertEquals(200, response.statusCode());
        assertEquals(LocalDate.of(1895, 12, 28), film.getReleaseDate());
    }

    @Test
    @DisplayName("filmControllerValidationTest6-save-realiseDate is before 28 DEC 1895")
    void filmControllerValidationTest6() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n  \"name\": \"Матрица\",\n  \"description\": \"adipisicing\",\n  \"releaseDate\": \"1895-12-27\",\n  \"duration\": 100\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject message = jsonElement.getAsJsonObject();

        assertEquals(400, response.statusCode());
        assertEquals("RealiseDate is before 28 december 1895!", message.get("message").getAsString());
    }

    @Test
    @DisplayName("filmControllerValidationTest7-save-duration is 0")
    void filmControllerValidationTest7() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n  \"name\": \"Матрица\",\n  \"description\": \"adipisicing\",\n  \"releaseDate\": \"2020-12-12\",\n  \"duration\": 0\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Film film = gson.fromJson(response.body(), Film.class);

        assertEquals(200, response.statusCode());
        assertEquals(0, film.getDuration());
    }

    @Test
    @DisplayName("filmControllerValidationTest8-save-duration is < 0")
    void filmControllerValidationTest8() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n  \"name\": \"Матрица\",\n  \"description\": \"adipisicing\",\n  \"releaseDate\": \"2020-12-12\",\n  \"duration\": -1\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject message = jsonElement.getAsJsonObject();

        assertEquals(400, response.statusCode());
        assertEquals("Duration is not positive!", message.get("message").getAsString());
    }

    @Test
    @DisplayName("filmControllerValidationTest9-update-normal operation")
    void filmControllerValidationTest9() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json1 = "{\n  \"name\": \"Матрица\",\n  \"description\": \"adipisicing\",\n  \"releaseDate\": \"2020-12-12\",\n  \"duration\": 100\n}";
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body1).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());
        String json2 = "{\n  \"id\": \"1\",\n  \"name\": \"Матрица Перезагрузка\",\n  \"description\": \"adipisicing\",\n  \"releaseDate\": \"2020-12-12\",\n  \"duration\": 100\n}";
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").PUT(body2).build();
        HttpResponse<String> response = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Film film = gson.fromJson(response.body(), Film.class);

        assertEquals(200, response.statusCode());
        assertEquals(1, film.getId());
        assertEquals("Матрица Перезагрузка", film.getName());
    }
}