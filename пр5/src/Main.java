import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    private static final String LOG_FILE = "log.txt";
    private static String currentFilePath = "";
    private static int changesCount = 0;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        createLogFile();

        while (true) {
            System.out.println("1. Создать и записать текст в файл");
            System.out.println("2. Прочитать файл");
            System.out.println("3. Дополнить файл (редактирование)");
            System.out.println("4. Удалить файл");
            System.out.println("5. Информация о файле");
            System.out.println("6. Выйти");
            System.out.print("Выберите действие: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    createAndWriteFile();
                    break;
                case 2:
                    readFile();
                    break;
                case 3:
                    appendToFile();
                    break;
                case 4:
                    deleteFile();
                    break;
                case 5:
                    showFileInfo();
                    break;
                case 6:
                    logAction("Программа завершена");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор");
            }
        }
    }

    private static void createAndWriteFile() {
        try {
            String path = getFilePath();
            System.out.print("Введите текст для записи в файл: ");
            String text = scanner.nextLine();

            try (FileWriter writer = new FileWriter(path)) {
                writer.write(text);
                currentFilePath = path;
                changesCount++;
                System.out.println("Файл успешно создан и записан");
                logAction("Создан и записан файл: " + path + ", текст: \"" + text + "\"");
                logFileStatistics(path);
            } catch (IOException e) {
                System.out.println("Ошибка при записи файла: " + e.getMessage());
                logAction("Ошибка при создании файла: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
            logAction("ОШИБКА: " + e.getMessage());
        }
    }

    private static void readFile() {
        try {
            String path = getFilePath();

            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                System.out.println("\nСодержимое файла:");
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                currentFilePath = path;
                logAction("Прочитан файл: " + path);
            } catch (IOException e) {
                System.out.println("Ошибка при чтении файла: " + e.getMessage());
                logAction("Ошибка при чтении файла: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
            logAction("Ошибка: " + e.getMessage());
        }
    }

    private static void appendToFile() {
        try {
            String path = getFilePath();

            System.out.print("Введите текст для добавления: ");
            String text = scanner.nextLine();

            try (FileWriter writer = new FileWriter(path, true)) {
                writer.write("\n" + text);
                currentFilePath = path;
                changesCount++;
                System.out.println("Текст успешно добавлен");
                logAction("Добавлен текст в файл " + path + ": \"" + text + "\"");
                logFileStatistics(path);
            } catch (IOException e) {
                System.out.println("Ошибка при добавлении текста: " + e.getMessage());
                logAction("Ошибка при добавлении текста: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
            logAction("Ошибка: " + e.getMessage());
        }
    }

    private static void deleteFile() {
        try {
            String path = getFilePath();

            System.out.print("Вы уверены, что хотите удалить файл? (y/n): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("y")) {
                Files.deleteIfExists(Paths.get(path));
                System.out.println("Файл удален");
                if (currentFilePath.equals(path)) {
                    currentFilePath = "";
                }
                logAction("Удален файл: " + path);
            } else {
                System.out.println("Удаление отменено");
                logAction("Отменено удаление файла: " + path);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при удалении файла: " + e.getMessage());
            logAction("Ошибка при удалении файла: " + e.getMessage());
        }
    }

    private static void showFileInfo() {
        try {
            String path = currentFilePath.isEmpty() ? getFilePath() : currentFilePath;

            File file = new File(path);
            if (!file.exists()) {
                System.out.println("Файл не существует");
                logAction("Попытка получить информацию о несуществующем файле: " + path);
                return;
            }

            int wordCount = countWords(path);
            System.out.println("\nИнформация о файле");
            System.out.println("Путь: " + file.getAbsolutePath());
            System.out.println("Размер: " + file.length() + " байт");
            System.out.println("Количество слов: " + wordCount);
            System.out.println("Количество изменений (сессия): " + changesCount);

            logAction("Просмотр информации о файле: " + path);
            logFileStatistics(path);
        } catch (Exception e) {
            System.out.println("Ошибка при получении информации: " + e.getMessage());
            logAction("ОШИБКА при получении информации: " + e.getMessage());
        }
    }

    private static int countWords(String path) throws IOException {
        int wordCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.trim().split("\\s+");
                if (!line.trim().isEmpty()) {
                    wordCount += words.length;
                }
            }
        }
        return wordCount;
    }

    private static String getFilePath() {
        System.out.print("Введите путь к файлу (например, C:/test.txt или test.txt): ");
        String path = scanner.nextLine();
        return path;
    }

    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Введите число: ");
            }
        }
    }

    private static void createLogFile() {
        try {
            File logFile = new File(LOG_FILE);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Ошибка создания лог-файла: " + e.getMessage());
        }
    }

    private static void logAction(String action) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            writer.write("[" + timestamp + "] " + action);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Ошибка записи в лог: " + e.getMessage());
        }
    }

    private static void logFileStatistics(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                return;
            }

            int wordCount = countWords(path);
            long size = file.length();

            logAction("Статистика файла " + path + ": слов=" + wordCount + ", размер=" + size + " байт, изменений=" + changesCount);
        } catch (IOException e) {
            logAction("Ошибка при подсчете статистики: " + e.getMessage());
        }
    }
}