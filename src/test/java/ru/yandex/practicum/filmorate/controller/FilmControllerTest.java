package ru.yandex.practicum.filmorate.controller;

import com.google.gson.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
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
    static ConfigurableApplicationContext ctx;

    @BeforeAll
    static void setUp() {
        ctx = SpringApplication.run(FilmorateApplication.class);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @AfterAll
    static void setDown() {
        ctx.close();
    }

    @Test
    @DisplayName("filmControllerValidationTest1-save-normal operation")
    void filmControllerValidationTest1() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n" +
                "  \"name\": \"labore nulla\",\n" +
                "  \"releaseDate\": \"1979-04-17\",\n" +
                "  \"description\": \"Duis in consequat esse\",\n" +
                "  \"duration\": 100,\n" +
                "  \"rate\": 4,\n" +
                "  \"mpa\": { \"id\": 1}\n" +
                "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Film film = gson.fromJson(response.body(), Film.class);
        assertEquals(200, response.statusCode());
        assertEquals("labore nulla", film.getName());
    }

    @Test
    @DisplayName("filmControllerValidationTest2-save-name is empty")
    void filmControllerValidationTest2() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n" +
                "  \"name\":\"\",\n" +
                "  \"releaseDate\": \"1979-04-17\",\n" +
                "  \"description\": \"Duis in consequat esse\",\n" +
                "  \"duration\": 100,\n" +
                "  \"rate\": 4,\n" +
                "  \"mpa\": { \"id\": 1}\n" +
                "}";
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
        String json = "{\n" +
                "  \"name\": \"labore nulla\",\n" +
                "  \"releaseDate\": \"1979-04-17\",\n" +
                "  \"description\": \"Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh" +
                " euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis" +
                " nostrud exerci tatio\",\n" +
                "  \"duration\": 100,\n" +
                "  \"rate\": 4,\n" +
                "  \"mpa\": { \"id\": 1}\n" +
                "}";
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
        String json = "{\n" +
                "  \"name\": \"labore nulla\",\n" +
                "  \"releaseDate\": \"1979-04-17\",\n" +
                "  \"description\": \"Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh" +
                " euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis" +
                " nostrud exerci tatio+\",\n" +
                "  \"duration\": 100,\n" +
                "  \"rate\": 4,\n" +
                "  \"mpa\": { \"id\": 1}\n" +
                "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject message = jsonElement.getAsJsonObject();

        assertEquals(400, response.statusCode());
        assertEquals("Description length extends 200 chars!", message.get("message").getAsString());
    }

    @Test
    @DisplayName("filmControllerValidationTest6-save-realiseDate is before 28 DEC 1895")
    void filmControllerValidationTest6() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n" +
                "  \"name\": \"labore nulla\",\n" +
                "  \"releaseDate\": \"1895-12-27\",\n" +
                "  \"description\": \"Duis in consequat esse\",\n" +
                "  \"duration\": 100,\n" +
                "  \"rate\": 4,\n" +
                "  \"mpa\": { \"id\": 1}\n" +
                "}";
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
        String json = "{\n" +
                "  \"name\": \"labore nulla\",\n" +
                "  \"releaseDate\": \"1979-04-17\",\n" +
                "  \"description\": \"Duis in consequat esse\",\n" +
                "  \"duration\": 0,\n" +
                "  \"rate\": 4,\n" +
                "  \"mpa\": { \"id\": 1}\n" +
                "}";
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
        String json = "{\n" +
                "  \"name\": \"labore nulla\",\n" +
                "  \"releaseDate\": \"1979-04-17\",\n" +
                "  \"description\": \"Duis in consequat esse\",\n" +
                "  \"duration\": -1,\n" +
                "  \"rate\": 4,\n" +
                "  \"mpa\": { \"id\": 1}\n" +
                "}";
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
        String json1 = "{\n" +
                "  \"name\": \"labore nulla\",\n" +
                "  \"releaseDate\": \"1979-04-17\",\n" +
                "  \"description\": \"Duis in consequat esse\",\n" +
                "  \"duration\": 100,\n" +
                "  \"rate\": 4,\n" +
                "  \"mpa\": { \"id\": 1}\n" +
                "}";
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body1).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());
        String json2 = "{\n  \"id\": \"1\",\n" +
                "  \"name\": \"Матрица Перезагрузка\",\n" +
                "  \"releaseDate\": \"1979-04-17\",\n" +
                "  \"description\": \"Duis in consequat esse\",\n" +
                "  \"duration\": 100,\n" +
                "  \"rate\": 4,\n" +
                "  \"mpa\": { \"id\": 1}\n" +
                "}";
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").PUT(body2).build();
        HttpResponse<String> response = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Film film = gson.fromJson(response.body(), Film.class);

        assertEquals(200, response.statusCode());
        assertEquals(1, film.getId());
        assertEquals("Матрица Перезагрузка", film.getName());
    }

    @Test
    @DisplayName("filmControllerValidationTest10-update-id is not found")
    void filmControllerValidationTest10() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n  \"id\": \"9999\",\n" +
                "  \"name\": \"Матрица Перезагрузка\",\n" +
                "  \"releaseDate\": \"1979-04-17\",\n" +
                "  \"description\": \"Duis in consequat esse\",\n" +
                "  \"duration\": 100,\n" +
                "  \"rate\": 4,\n" +
                "  \"mpa\": { \"id\": 1}\n" +
                "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").PUT(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject message = jsonElement.getAsJsonObject();

        assertEquals(404, response.statusCode());
        assertEquals("Film with id 9999 didn't found!", message.get("message").getAsString());
    }
}