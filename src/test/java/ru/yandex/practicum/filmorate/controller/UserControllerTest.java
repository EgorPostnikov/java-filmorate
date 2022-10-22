package ru.yandex.practicum.filmorate.controller;

import com.google.gson.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
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
    @DisplayName("filmControllerValidationTest10-update-id is not found")
    void filmControllerValidationTest10() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n  \"id\": \"9999\",\n  \"name\": \"Матрица\",\n  \"description\": \"adipisicing\",\n  \"releaseDate\": \"2020-12-12\",\n  \"duration\": 100\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").PUT(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject message = jsonElement.getAsJsonObject();

        assertEquals(404, response.statusCode());
        assertEquals("Film with id 9999 didn't found!", message.get("message").getAsString());
    }

    @Test
    @DisplayName("userControllerValidationTest1-save-normal operation")
    void userControllerValidationTest1() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n  \"login\": \"dolore\",\n  \"name\": \"Nick Name\",\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"1946-08-20\"\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        User user = gson.fromJson(response.body(), User.class);

        assertEquals(200, response.statusCode());
        assertEquals("Nick Name", user.getName());
    }

    @Test
    @DisplayName("userControllerValidationTest2-save-mail is empty")
    void userControllerValidationTest2() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n  \"login\": \"dolore\",\n  \"name\": \"Nick Name\",\n  \"email\": \"\",\n  \"birthday\": \"1946-08-20\"\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject message = jsonElement.getAsJsonObject();

        assertEquals(400, response.statusCode());
        assertEquals("Email is not correct!", message.get("message").getAsString());
    }

    @Test
    @DisplayName("userControllerValidationTest3-save-mail is not contain @")
    void userControllerValidationTest3() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n  \"login\": \"dolore\",\n  \"name\": \"Nick Name\",\n  \"email\": \"mailmail.ru\",\n  \"birthday\": \"1946-08-20\"\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject message = jsonElement.getAsJsonObject();

        assertEquals(400, response.statusCode());
        assertEquals("Email is not correct!", message.get("message").getAsString());
    }

    @Test
    @DisplayName("userControllerValidationTest4-save-login is empty")
    void userControllerValidationTest4() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n  \"login\": \"\",\n  \"name\": \"Nick Name\",\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"1946-08-20\"\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject message = jsonElement.getAsJsonObject();

        assertEquals(400, response.statusCode());
        assertEquals("Login is not correct!", message.get("message").getAsString());
    }

    @Test
    @DisplayName("userControllerValidationTest5-save-login is blank")
    void userControllerValidationTest5() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n  \"login\": \"      \",\n  \"name\": \"Nick Name\",\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"1946-08-20\"\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject message = jsonElement.getAsJsonObject();

        assertEquals(400, response.statusCode());
        assertEquals("Login is not correct!", message.get("message").getAsString());
    }

    @Test
    @DisplayName("userControllerValidationTest6-save-name is empty")
    void userControllerValidationTest6() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n  \"login\": \"dolore\",\n  \"name\": \"\",\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"1946-08-20\"\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        User user = gson.fromJson(response.body(), User.class);

        assertEquals(200, response.statusCode());
        assertEquals("dolore", user.getName());
    }

    @Test
    @DisplayName("userControllerValidationTest7-save-birthday on future")
    void userControllerValidationTest7() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n  \"login\": \"dolore\",\n  \"name\": \"Nick Name\",\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"2030-08-20\"\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject message = jsonElement.getAsJsonObject();

        assertEquals(400, response.statusCode());
        assertEquals("Birthday date is not correct!", message.get("message").getAsString());
    }

    @Test
    @DisplayName("userControllerValidationTest8-update-normal operation")
    void UserControllerValidationTest8() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/users");
        String json1 = "{\n  \"login\": \"dolore\",\n  \"name\": \"Nick Name\",\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"1946-08-20\"\n}";
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").POST(body1).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());
        String json2 = "{\n  \"id\": \"1\",\n  \"login\": \"dolore\",\n  \"name\": \"new Nick Name\",\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"1946-08-20\"\n}";
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").PUT(body2).build();
        HttpResponse<String> response = client.send(request2, HttpResponse.BodyHandlers.ofString());
        User user = gson.fromJson(response.body(), User.class);

        assertEquals(200, response.statusCode());
        assertEquals(1, user.getId());
        assertEquals("new Nick Name", user.getName());
    }

    @Test
    @DisplayName("userControllerValidationTest9-update-id is not found")
    void userControllerValidationTest9() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n  \"id\": \"9999\",\n  \"login\": \"dolore\",\n  \"name\": \"Nick Name\",\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"1946-08-20\"\n}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json").PUT(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject message = jsonElement.getAsJsonObject();

        assertEquals(404, response.statusCode());
        assertEquals("User with id 9999 didn't found!", message.get("message").getAsString());
    }
}