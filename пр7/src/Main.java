import java.io.*;
import java.util.*;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static List<SportProduct> products = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static User currentUser = null;
    private static boolean running = true;

    public static void main(String[] args) {
        loadAllData();

        while (running) {
            if (currentUser == null) {
                showAuthMenu();
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    register();
                } else if (choice == 2) {
                    login();
                } else if (choice == 3) {
                    System.out.println("До свидания!");
                    saveAllData();
                    running = false;
                } else {
                    System.out.println("Неверный выбор!");
                }
            } else {
                currentUser.displayRoleInfo();
                if (currentUser instanceof Employee) {
                    showEmployeeMenu();
                } else if (currentUser instanceof Customer) {
                    showCustomerMenu();
                }
            }
        }
    }

    private static void showAuthMenu() {
        System.out.println("Спортивный магазин");
        System.out.println("1. Регистрация");
        System.out.println("2. Вход");
        System.out.println("3. Выход");
        System.out.print("Выберите действие: ");
    }

    private static void showEmployeeMenu() {
        System.out.println("Меню сотрудника");
        System.out.println("1. Просмотреть все товары");
        System.out.println("2. Добавить товар");
        System.out.println("3. Редактировать товар");
        System.out.println("4. Удалить товар");
        System.out.println("5. Экспорт товаров в CSV");
        System.out.println("6. Импорт товаров из CSV");
        System.out.println("7. Выйти из аккаунта");
        System.out.println("8. Выход из программы");
        System.out.print("Выберите действие: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1: showAllProducts(); break;
            case 2: addProduct(); break;
            case 3: editProduct(); break;
            case 4: deleteProduct(); break;
            case 5: exportToCSV(); break;
            case 6: importFromCSV(); break;
            case 7: logout(); break;
            case 8: exitProgram(); break;
            default: System.out.println("Неверный выбор!");
        }
    }

    private static void showCustomerMenu() {
        System.out.println("Меню покупателя");
        System.out.println("1. Просмотреть каталог товаров");
        System.out.println("2. Купить товар");
        System.out.println("3. Пополнить баланс");
        System.out.println("4. Выйти из аккаунта");
        System.out.println("5. Выход из программы");
        System.out.print("Выберите действие: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1: showAllProducts(); break;
            case 2: buyProduct(); break;
            case 3: replenishBalance(); break;
            case 4: logout(); break;
            case 5: exitProgram(); break;
            default: System.out.println("Неверный выбор!");
        }
    }

    private static void register() {
        System.out.println("Регистрация");
        System.out.print("Имя пользователя: ");
        String username = sc.nextLine();

        for (User u : users) {
            if (u.getUsername().equals(username)) {
                System.out.println("Пользователь с таким именем уже существует!");
                return;
            }
        }

        System.out.print("Пароль: ");
        String password = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.println("Выберите тип аккаунта:");
        System.out.println("1. Покупатель");
        System.out.println("2. Сотрудник");
        int type = sc.nextInt();
        sc.nextLine();

        if (type == 1) {
            System.out.print("Стартовый баланс (руб): ");
            double balance = sc.nextDouble();
            sc.nextLine();
            users.add(new Customer(username, password, email, balance));
            System.out.println("Регистрация покупателя успешна!");
        } else if (type == 2) {
            System.out.print("Должность: ");
            String position = sc.nextLine();
            users.add(new Employee(username, password, email, position));
            System.out.println("Регистрация сотрудника успешна!");
        } else {
            System.out.println("Неверный тип аккаунта!");
            return;
        }
        saveAllData();
    }

    private static void login() {
        System.out.println("Вход");
        System.out.print("Имя пользователя: ");
        String username = sc.nextLine();
        System.out.print("Пароль: ");
        String password = sc.nextLine();

        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                currentUser = u;
                System.out.println("Добро пожаловать, " + username + "!");
                return;
            }
        }
        System.out.println("Неверное имя пользователя или пароль!");
    }

    private static void logout() {
        currentUser = null;
        System.out.println("Выход из аккаунта выполнен.");
    }

    private static void exitProgram() {
        System.out.println("До свидания!");
        saveAllData();
        running = false;
    }

    private static void showAllProducts() {
        System.out.println("Список товаров");
        if (products.isEmpty()) {
            System.out.println("Товары отсутствуют");
            return;
        }
        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i));
        }
    }

    private static void buyProduct() {
        showAllProducts();
        if (products.isEmpty()) return;

        System.out.print("Выберите номер товара для покупки: ");
        int index = sc.nextInt() - 1;
        System.out.print("Укажите количество: ");
        int qty = sc.nextInt();
        sc.nextLine();

        if (index < 0 || index >= products.size()) {
            System.out.println("Неверный номер товара!");
            return;
        }

        SportProduct p = products.get(index);
        Customer customer = (Customer) currentUser;
        double totalCost = p.getPrice() * qty;

        if (p.getStock() < qty) {
            System.out.println("Недостаточно товара на складе!");
        } else if (customer.getBalance() < totalCost) {
            System.out.println("Недостаточно средств! Требуется: " + totalCost + " руб");
        } else {
            p.reduceStock(qty);
            customer.withdraw(totalCost);
            System.out.println("Покупка совершена успешно! Списано: " + totalCost + " руб");
            saveAllData();
        }
    }

    private static void replenishBalance() {
        if (currentUser instanceof Customer) {
            System.out.print("Введите сумму для пополнения (руб): ");
            double amount = sc.nextDouble();
            sc.nextLine();
            Customer c = (Customer) currentUser;
            c.setBalance(c.getBalance() + amount);
            System.out.println("Баланс успешно обновлен!");
            saveAllData();
        }
    }

    private static void addProduct() {
        System.out.println("Добавление товара");
        System.out.print("Название: ");
        String name = sc.nextLine();
        System.out.print("Бренд: ");
        String brand = sc.nextLine();
        System.out.print("Категория: ");
        String category = sc.nextLine();
        System.out.print("Размер: ");
        String size = sc.nextLine();
        System.out.print("Цена (руб): ");
        double price = sc.nextDouble();
        System.out.print("Количество: ");
        int stock = sc.nextInt();
        sc.nextLine();

        products.add(new SportProduct(name, brand, category, size, price, stock));
        System.out.println("Товар успешно добавлен!");
        saveProducts();
    }

    private static void editProduct() {
        showAllProducts();
        if (products.isEmpty()) return;

        System.out.print("Выберите номер товара для редактирования: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        if (index < 0 || index >= products.size()) {
            System.out.println("Неверный номер!");
            return;
        }

        SportProduct p = products.get(index);
        System.out.println("Редактирование товара");
        System.out.println("(оставьте поле пустым, чтобы не менять)");

        System.out.print("Название (" + p.getName() + "): ");
        String input = sc.nextLine();
        if (!input.isEmpty()) p.setName(input);

        System.out.print("Бренд (" + p.getBrand() + "): ");
        input = sc.nextLine();
        if (!input.isEmpty()) p.setBrand(input);

        System.out.print("Категория (" + p.getCategory() + "): ");
        input = sc.nextLine();
        if (!input.isEmpty()) p.setCategory(input);

        System.out.print("Размер (" + p.getSize() + "): ");
        input = sc.nextLine();
        if (!input.isEmpty()) p.setSize(input);

        System.out.print("Цена (" + p.getPrice() + " руб): ");
        input = sc.nextLine();
        if (!input.isEmpty()) p.setPrice(Double.parseDouble(input));

        System.out.print("Количество (" + p.getStock() + "): ");
        input = sc.nextLine();
        if (!input.isEmpty()) p.setStock(Integer.parseInt(input));

        System.out.println("Товар успешно обновлен!");
        saveProducts();
    }

    private static void deleteProduct() {
        showAllProducts();
        if (products.isEmpty()) return;

        System.out.print("Выберите номер товара для удаления: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        if (index < 0 || index >= products.size()) {
            System.out.println("Неверный номер!");
            return;
        }

        SportProduct removed = products.remove(index);
        System.out.println("Товар \"" + removed.getName() + "\" удален!");
        saveProducts();
    }

    private static void exportToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("products.csv"))) {
            writer.println("Название,Бренд,Категория,Размер,Цена(руб),Количество");
            for (SportProduct p : products) {
                writer.printf("%s,%s,%s,%s,%.2f,%d%n",
                        p.getName(), p.getBrand(), p.getCategory(),
                        p.getSize(), p.getPrice(), p.getStock());
            }
            System.out.println("Экспорт успешно выполнен");
        } catch (IOException e) {
            System.out.println("Ошибка при экспорте: " + e.getMessage());
        }
    }

    private static void importFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("products.csv"))) {
            String line = reader.readLine();
            int added = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                try {
                    String name = parts[0].trim();
                    String brand = parts[1].trim();
                    String category = parts[2].trim();
                    String size = parts[3].trim();
                    double price = Double.parseDouble(parts[4].trim());
                    int stock = Integer.parseInt(parts[5].trim());

                    boolean exists = false;
                    for (SportProduct p : products) {
                        if (p.getName().equalsIgnoreCase(name) && p.getBrand().equalsIgnoreCase(brand)) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        products.add(new SportProduct(name, brand, category, size, price, stock));
                        added++;
                    }
                } catch (NumberFormatException ignored) {}
            }
            System.out.println("Импорт завершен! Добавлено: " + added);
            saveProducts();
        } catch (IOException e) {
            System.out.println("Ошибка при импорте: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadAllData() {
        File usersFile = new File("users.dat");
        File productsFile = new File("products.dat");

        if (usersFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(usersFile))) {
                users = (List<User>) ois.readObject();
            } catch (Exception e) {
                System.out.println("Ошибка загрузки пользователей: " + e.getMessage());
            }
        }

        if (productsFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(productsFile))) {
                products = (List<SportProduct>) ois.readObject();
            } catch (Exception e) {
                System.out.println("Ошибка загрузки товаров: " + e.getMessage());
            }
        }

        System.out.println("Загружено пользователей: " + users.size());
        System.out.println("Загружено товаров: " + products.size());
    }

    private static void saveAllData() {
        saveUsers();
        saveProducts();
    }

    private static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения пользователей: " + e.getMessage());
        }
    }

    private static void saveProducts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("products.dat"))) {
            oos.writeObject(products);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения товаров: " + e.getMessage());
        }
    }
}