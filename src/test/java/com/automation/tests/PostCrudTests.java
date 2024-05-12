package com.automation.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PostCrudTests {
    @Test
    public void testCreatePost() {
        // код для создания нового поста и проверки результатов
        // Определение данных для нового поста
        String postData = "{\"title\": \"New Post Title\", \"body\": \"New Post Body\", \"userId\": 1}";

        // Определение URL эндпоинта для создания поста
        String endpoint = "https://jsonplaceholder.typicode.com/posts";

        // Отправка POST запроса для создания нового поста
        given()
                .contentType(ContentType.JSON)
                .body(postData)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(201); // Проверка успешного создания поста

    }

    @Test
    public void testReadPost() {


        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // Отправляем GET-запрос к эндпоинту /posts/1
        Response response = given()
                .when()
                .get("/posts/1")
                .then()
                .extract().response();

        // Проверяем статус-код ответа
        response.then().statusCode(200);

        // Проверяем, что заголовок Content-Type установлен как application/json
        response.then().assertThat().header("Content-Type", equalTo("application/json; charset=utf-8"));

        // Проверяем, что тело ответа содержит определенные данные, например, поле "userId" равно 1
        response.then().assertThat().body("userId", equalTo(1));
        System.out.println(response.getBody().asString());
    }


    @Test
    public void testUpdatePost() {
        // код для обновления поста и проверки результатов
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // Define the updated post data
        String updatedPostData = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"Updated Post Title\",\n" +
                "    \"body\": \"Updated Post Body\",\n" +
                "    \"userId\": 1\n" +
                "}";

        // Send PUT request to update the post
        Response response = given()
                .contentType("application/json")
                .body(updatedPostData)
                .when()
                .put("/posts/1")
                .then()
                .extract().response();

        // Assert the status code
        response.then().statusCode(200);

        // Assert the response body or other properties if needed
        System.out.println("Updated Post Response:");
        System.out.println(response.getBody().asString());
    }

    @Test
    public void testDeletePost() {
        // код для удаления поста и проверки результатов
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // Send DELETE request to delete the post
        Response response = given()
                .when()
                .delete("/posts/1")
                .then()
                .extract().response();

        // Assert the status code to ensure that the deletion was successful
        response.then().statusCode(200);
    }

}
