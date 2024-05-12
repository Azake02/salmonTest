package com.automation.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.*;
import java.util.stream.Collectors;

public class TopWords {
    // Метод для извлечения тела поста из JSON-ответа
    public static String extractBodyFromPost(Response response) {
        return response.jsonPath().getString("body");
    }

    // Метод для анализа частоты слов в теле поста
    public static Map<String, Integer> analyzePostBodyFrequency(List<String> postBodies) {
        // Создаем пустой словарь для хранения частоты слов
        Map<String, Integer> wordFrequencyMap = new HashMap<>();

        // Проходим по каждому телу поста
        for (String body : postBodies) {
            // Разбиваем тело поста на слова, игнорируя знаки препинания и пробелы
            String[] words = body.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");

            // Обновляем частоту каждого слова в словаре
            for (String word : words) {
                wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
            }
        }

        return wordFrequencyMap;
    }

    // Метод для составления топа по частоте слов
    public static List<Map.Entry<String, Integer>> getTopWords(Map<String, Integer> wordFrequencyMap) {
        // Сортируем записи словаря по убыванию частоты и выбираем топ 10
        List<Map.Entry<String, Integer>> sortedEntries = wordFrequencyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());

        return sortedEntries;
    }

    // Метод для вывода топа слов в формате "Word - Frequency"
    public static void printTopWords(List<Map.Entry<String, Integer>> topWords) {
        int rank = 1;
        for (Map.Entry<String, Integer> entry : topWords) {
            System.out.println(rank + ". " + entry.getKey() + " - " + entry.getValue());
            rank++;
        }
    }

    public static void main(String[] args) {
        // Отправляем GET-запрос к эндпоинту /posts
        Response response = RestAssured.get("https://jsonplaceholder.typicode.com/posts");
        // Получаем список всех постов
        List<String> postBodies = response.jsonPath().getList("body");

        // Анализируем частоту слов в телах постов
        Map<String, Integer> wordFrequencyMap = analyzePostBodyFrequency(postBodies);

        // Получаем топ 10 слов по частоте
        List<Map.Entry<String, Integer>> topWords = getTopWords(wordFrequencyMap);

        // Выводим результаты
        printTopWords(topWords);
    }
}
