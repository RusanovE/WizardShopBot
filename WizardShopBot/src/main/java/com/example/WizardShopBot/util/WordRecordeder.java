package com.example.WizardShopBot.util;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class WordRecordeder {

    public static void writeFile(String data) {

        String filePath = ""; // Путь к файлу, который вы хотите записать

        // Создаем объект FileWriter с использованием try-with-resources
        try (FileWriter fileWriter = new FileWriter(filePath,true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            // Записываем данные в файл
            bufferedWriter.write(data);

            log.info("Данные успешно записаны в файл: " + filePath);
        } catch (IOException e) {
            log.error("Произошла ошибка при записи данных в файл: " + e.getMessage());
        }
    }

}