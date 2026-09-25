import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Employee> employees = createEmployees();

        System.out.println("Список сотрудников");
        System.out.println("------------------------------------------------------------");
        employees.forEach(System.out::println);
        System.out.println();

        task1(employees);
        task2(employees);
        task3(employees);
        task4(employees);
    }

    private static List<Employee> createEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Иванов Иван", "IT", 85000, "2023-05-15", 4.5));
        employees.add(new Employee("Петров Петр", "IT", 72000, "2022-11-20", 3.8));
        employees.add(new Employee("Сидорова Мария", "HR", 65000, "2024-01-10", 4.2));
        employees.add(new Employee("Козлов Алексей", "IT", 95000, "2021-03-05", 4.9));
        employees.add(new Employee("Смирнова Елена", "HR", 70000, "2020-07-12", 3.5));
        employees.add(new Employee("Федоров Дмитрий", "Finance", 80000, "2023-09-25", 4.0));
        employees.add(new Employee("Васильева Анна", "Finance", 68000, "2022-06-01", 3.2));
        employees.add(new Employee("Морозов Сергей", "IT", 78000, "2024-03-14", 4.7));
        employees.add(new Employee("Новикова Ольга", "HR", 62000, "2021-12-03", 3.9));
        employees.add(new Employee("Павлов Андрей", "Finance", 90000, "2020-08-19", 4.8));
        return employees;
    }

    private static void task1(List<Employee> employees) {
        System.out.println("Фильтрация: сотрудники отдела IT с зарплатой выше средней");
        String targetDepartment = "IT";
        double averageSalary = employees.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
        System.out.println();

        List<Employee> filtered = employees.stream()
                .filter(e -> e.getDepartment().equals(targetDepartment))
                .filter(e -> e.getSalary() > averageSalary)
                .collect(Collectors.toList());

        System.out.println("Сотрудники отдела " + targetDepartment + " с зарплатой выше средней:");
        filtered.forEach(e -> System.out.println("  " + e));
        System.out.println();
    }

    private static void task2(List<Employee> employees) {
        System.out.println("Группировка: количество сотрудников с рейтингом выше 3.0 по отделам");
        Map<String, Long> countByDepartment = employees.stream()
                .filter(e -> e.getPerformanceRating() > 3.0)
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.counting()
                ));

        countByDepartment.forEach((dept, count) ->
                System.out.println("  " + dept + ": " + count + " сотрудников"));
        System.out.println();
    }

    private static void task3(List<Employee> employees) {
        System.out.println("Поиск: сотрудник с наибольшей зарплатой, нанятый за последние 2 года");
        LocalDate twoYearsAgo = LocalDate.now().minusYears(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Employee result = employees.stream()
                .filter(e -> {
                    LocalDate hireDate = LocalDate.parse(e.getHireDate(), formatter);
                    return hireDate.isAfter(twoYearsAgo) || hireDate.isEqual(twoYearsAgo);
                })
                .max(Comparator.comparingDouble(Employee::getSalary))
                .orElse(null);

        if (result != null) {
            System.out.println("  " + result);
        } else {
            System.out.println("  Сотрудники не найдены");
        }
        System.out.println();
    }

    private static void task4(List<Employee> employees) {
        System.out.println("Агрегация: средняя зарплата сотрудников по отделам");
        Map<String, Double> averageByDepartment = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getSalary)
                ));

        averageByDepartment.forEach((dept, avg) ->
                System.out.printf("  %s: %.2f%n", dept, avg));
    }
}