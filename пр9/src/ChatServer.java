import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = 12345;
    private static Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
    private static Map<String, Set<PrintWriter>> channels = new ConcurrentHashMap<>();
    private static final String USERS_FILE = "users.txt";
    private static final String HISTORY_DIR = "history/";
    private static final int HISTORY_DAYS_LIMIT = 3;

    public static void main(String[] args) throws IOException {
        new File(HISTORY_DIR).mkdirs();
        System.out.println("Сервер запущен на порту " + PORT);

        channels.put("общий", Collections.synchronizedSet(new HashSet<>()));
        channels.put("первый", Collections.synchronizedSet(new HashSet<>()));
        channels.put("второй", Collections.synchronizedSet(new HashSet<>()));
        channels.put("третий", Collections.synchronizedSet(new HashSet<>()));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;
        private String currentChannel;
        private Set<String> activePMs = new HashSet<>();

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                showAllCommands();

                if (!handleAuth()) return;

                selectChannel();

                String input;
                while ((input = in.readLine()) != null) {
                    if (input.equals("/quit")) {
                        out.println("Вы вышли из чата. До свидания!");
                        break;
                    } else if (input.startsWith("/pm ")) {
                        handlePrivateMessage(input);
                    } else if (input.startsWith("/join ")) {
                        changeChannel(input);
                    } else if (input.equals("/channels")) {
                        showChannels();
                    } else if (input.equals("/history")) {
                        loadHistory(currentChannel);
                    } else if (input.startsWith("/pm_history ")) {
                        handlePMHistory(input);
                    } else if (input.equals("/online")) {
                        showOnlineUsers();
                    } else if (input.equals("/commands")) {
                        showAllCommands();
                    } else if (currentChannel != null) {
                        broadcast(input, currentChannel);
                    }
                }
            } catch (IOException e) {
                System.out.println(username + " отключился.");
            } finally {
                cleanup();
            }
        }

        private void showAllCommands() {
            out.println("Команды");
            out.println("/pm <ник> <сообщение> - личное сообщение");
            out.println("/pm_history <ник> - история ЛС с пользователем");
            out.println("/join <канал> - сменить канал");
            out.println("/channels - показать доступные каналы");
            out.println("/history - история текущего канала");
            out.println("/online - показать онлайн пользователей");
            out.println("/commands - показать этот список ");
            out.println("/quit - выход из чата ");
        }

        private boolean handleAuth() throws IOException {
            out.println("1 - Вход");
            out.println("2 - Регистрация");
            out.print("Выберите действие: ");
            String choice = in.readLine();
            out.print("Логин: ");
            String login = in.readLine();
            out.print("Пароль: ");
            String pass = in.readLine();

            if ("2".equals(choice)) {
                if (userExists(login)) {
                    out.println("Ошибка: пользователь с таким логином уже существует");
                    return false;
                }
                saveUser(login, pass);
                out.println("Регистрация успешна!");
            }

            if (checkUser(login, pass)) {
                this.username = login;
                clients.put(username, this);
                out.println("Добро пожаловать, " + username + "!");
                return true;
            } else {
                out.println("Ошибка: неверный логин или пароль");
                return false;
            }
        }

        private boolean userExists(String login) throws IOException {
            File f = new File(USERS_FILE);
            if (!f.exists()) return false;
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith(login + ":")) return true;
                }
            }
            return false;
        }

        private void selectChannel() throws IOException {
            out.println("Доступные каналы");
            out.println("1. общий - Общий чат (все пользователи)");
            out.println("2. первый - Первый канал");
            out.println("3. второй - Второй канал");
            out.println("4. третий - Третий канал");
            out.print("Введите название канала (общий/первый/второй/третий): ");

            String choice = in.readLine();
            if (channels.containsKey(choice)) {
                currentChannel = choice;
                channels.get(currentChannel).add(out);
                out.println("Вы присоединились к каналу '" + currentChannel + "'");
                out.println("Для просмотра команд введите /commands");
                loadHistory(currentChannel);
            } else {
                out.println("Неверный канал. Подключение к 'общий' по умолчанию.");
                currentChannel = "общий";
                channels.get(currentChannel).add(out);
                loadHistory(currentChannel);
            }
        }

        private void changeChannel(String input) throws IOException {
            String[] parts = input.split(" ", 2);
            if (parts.length < 2) {
                out.println("Использование: /join <канал>");
                return;
            }
            String newChannel = parts[1];
            if (!channels.containsKey(newChannel)) {
                out.println("Канал '" + newChannel + "' не существует. Доступные: общий, первый, второй, третий");
                return;
            }

            if (currentChannel != null && channels.get(currentChannel) != null) {
                channels.get(currentChannel).remove(out);
            }
            currentChannel = newChannel;
            channels.get(currentChannel).add(out);
            out.println("Вы перешли в канал '" + currentChannel + "'");
            loadHistory(currentChannel);
        }

        private void showChannels() {
            out.println("Доступные каналы: общий, первый, второй, третий");
            out.println("Вы сейчас в канале: " + currentChannel);
        }

        private void showOnlineUsers() {
            if (clients.isEmpty()) {
                out.println("Нет онлайн пользователей");
                return;
            }
            out.println("Онлайн пользователи (" + clients.size() + "):");
            for (String name : clients.keySet()) {
                out.println("  - " + name + (name.equals(username) ? " (вы)" : ""));
            }
        }

        private void broadcast(String text, String channel) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String formatted = String.format("[%s][%s] %s: %s", channel, time, username, text);

            saveHistory(channel, formatted, timestamp);

            synchronized (channels.get(channel)) {
                for (PrintWriter writer : channels.get(channel)) {
                    writer.println(formatted);
                }
            }
        }

        private void handlePrivateMessage(String input) {
            String[] parts = input.split(" ", 3);
            if (parts.length < 3) {
                out.println("Использование: /pm <никнейм> <сообщение>");
                return;
            }
            String target = parts[1];
            String message = parts[2];

            if (!clients.containsKey(target) && !userExistsStatic(target)) {
                out.println("Пользователь '" + target + "' не найден в системе");
                return;
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String formatted = String.format("[ЛС][%s] %s -> %s: %s", time, username, target, message);

            String pmKey = getPMKey(username, target);
            if (!activePMs.contains(pmKey)) {
                activePMs.add(pmKey);
            }

            savePMHistory(username, target, formatted, timestamp);

            out.println(formatted);

            if (clients.containsKey(target)) {
                clients.get(target).out.println(formatted);
                String receiverKey = getPMKey(target, username);
                clients.get(target).activePMs.add(receiverKey);
            }
        }

        private void handlePMHistory(String input) {
            String[] parts = input.split(" ", 2);
            if (parts.length < 2) {
                out.println("Использование: /pm_history <никнейм>");
                return;
            }
            String target = parts[1];

            if (!userExistsStatic(target)) {
                out.println("Пользователь '" + target + "' не найден в системе");
                return;
            }

            loadPMHistory(target);
        }

        private boolean userExistsStatic(String login) {
            File f = new File(USERS_FILE);
            if (!f.exists()) return false;
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith(login + ":")) return true;
                }
            } catch (IOException e) {
                return false;
            }
            return false;
        }

        private String getPMKey(String user1, String user2) {
            List<String> names = Arrays.asList(user1, user2);
            Collections.sort(names);
            return "pm_" + names.get(0) + "_" + names.get(1);
        }

        private void saveHistory(String channel, String msg, String timestamp) {
            try (FileWriter fw = new FileWriter(HISTORY_DIR + channel + ".log", true);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println(timestamp + "|" + msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void savePMHistory(String from, String to, String msg, String timestamp) {
            String key = getPMKey(from, to);
            try (FileWriter fw = new FileWriter(HISTORY_DIR + key + ".log", true);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println(timestamp + "|" + msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void loadHistory(String channel) {
            File file = new File(HISTORY_DIR + channel + ".log");
            if (!file.exists()) {
                out.println("История канала '" + channel + "' пуста");
                return;
            }

            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(HISTORY_DAYS_LIMIT);
            List<String> recentMessages = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty()) continue;
                    int separatorIndex = line.indexOf("|");
                    if (separatorIndex == -1) continue;

                    String timestampStr = line.substring(0, separatorIndex);
                    String message = line.substring(separatorIndex + 1);

                    try {
                        LocalDateTime msgTime = LocalDateTime.parse(timestampStr,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        if (msgTime.isAfter(cutoffDate)) {
                            recentMessages.add(message);
                        }
                    } catch (Exception e) {
                        recentMessages.add(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (recentMessages.isEmpty()) {
                out.println("Нет сообщений в канале '" + channel + "' за последние " + HISTORY_DAYS_LIMIT + " дней");
            } else {
                out.println("История канала '" + channel + "' (последние " + HISTORY_DAYS_LIMIT + " дней)");
                for (String msg : recentMessages) {
                    out.println(msg);
                }
                out.println("Конец истории (" + recentMessages.size() + " сообщений)");
            }
        }

        private void loadPMHistory(String target) {
            String key = getPMKey(username, target);
            File file = new File(HISTORY_DIR + key + ".log");
            if (!file.exists()) {
                out.println("Нет истории переписки с " + target);
                return;
            }

            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(HISTORY_DAYS_LIMIT);
            List<String> recentMessages = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty()) continue;
                    int separatorIndex = line.indexOf("|");
                    if (separatorIndex == -1) continue;

                    String timestampStr = line.substring(0, separatorIndex);
                    String message = line.substring(separatorIndex + 1);

                    try {
                        LocalDateTime msgTime = LocalDateTime.parse(timestampStr,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        if (msgTime.isAfter(cutoffDate)) {
                            recentMessages.add(message);
                        }
                    } catch (Exception e) {
                        recentMessages.add(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (recentMessages.isEmpty()) {
                out.println("Нет сообщений в переписке с " + target + " за последние " + HISTORY_DAYS_LIMIT + " дней");
            } else {
                out.println("История переписки с " + target + " (последние " + HISTORY_DAYS_LIMIT + " дней)");
                for (String msg : recentMessages) {
                    out.println(msg);
                }
                out.println("Конец истории (" + recentMessages.size() + " сообщений)");
            }
        }

        private void saveUser(String user, String pass) throws IOException {
            try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE, true))) {
                pw.println(user + ":" + pass);
            }
        }

        private boolean checkUser(String user, String pass) throws IOException {
            File f = new File(USERS_FILE);
            if (!f.exists()) return false;
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.equals(user + ":" + pass)) return true;
                }
            }
            return false;
        }

        private void cleanup() {
            if (currentChannel != null && channels.get(currentChannel) != null) {
                channels.get(currentChannel).remove(out);
            }
            if (username != null) {
                clients.remove(username);
                System.out.println("Пользователь " + username + " покинул чат.");
            }
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}