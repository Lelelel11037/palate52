import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static volatile boolean running = true;

    public static void main(String[] args) {
        System.out.println("Подключение к серверу " + SERVER_ADDRESS + ":" + SERVER_PORT);

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Подключение установлено!\n");

            Thread readerThread = new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null && running) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    if (running) {
                        System.out.println("Соединение с сервером разорвано.");
                    }
                }
            });
            readerThread.setDaemon(true);
            readerThread.start();

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            while (running && scanner.hasNextLine()) {
                System.out.print("> ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("/quit")) {
                    out.println("/quit");
                    System.out.println("Отключение от сервера...");
                    running = false;
                    break;
                }
                out.println(userInput);
            }

            System.out.println("До свидания!");

        } catch (IOException e) {
            System.err.println("Не удалось подключиться к серверу: " + e.getMessage());
            System.err.println("Убедитесь, что сервер запущен на порту " + SERVER_PORT);
        }
    }
}