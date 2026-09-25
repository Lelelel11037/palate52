import java.util.Scanner;

public class main {

    public static void calculatorIfElse(double num1, String operator, double num2) {
        double result = 0;
        boolean validOperation = true;

        if (operator.equals("+")) {
            result = num1 + num2;
        } else if (operator.equals("-")) {
            result = num1 - num2;
        } else if (operator.equals("*")) {
            result = num1 * num2;
        } else if (operator.equals("/")) {
            if (num2 != 0) {
                result = num1 / num2;
            } else {
                System.out.println("Ошибка: деление на ноль!");
                validOperation = false;
            }
        } else if (operator.equals("%")) {
            int intNum1 = (int) num1;
            int intNum2 = (int) num2;
            if (intNum2 != 0) {
                result = intNum1 % intNum2;
            } else {
                System.out.println("Ошибка: деление на ноль при вычислении остатка!");
                validOperation = false;
            }
        } else {
            System.out.println("Ошибка: неизвестный оператор. Используйте +, -, *, /, %");
            validOperation = false;
        }

        if (validOperation) {
            System.out.println("[IF/ELSE] Результат: " + num1 + " " + operator + " " + num2 + " = " + result);
        }
    }

    public static void calculatorSwitch(double num1, String operator, double num2) {
        double result = 0;
        boolean validOperation = true;

        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    System.out.println("Ошибка: деление на ноль!");
                    validOperation = false;
                }
                break;
            case "%":
                int intNum1 = (int) num1;
                int intNum2 = (int) num2;
                if (intNum2 != 0) {
                    result = intNum1 % intNum2;
                } else {
                    System.out.println("Ошибка: деление на ноль при вычислении остатка!");
                    validOperation = false;
                }
                break;
            default:
                System.out.println("Ошибка: неизвестный оператор. Используйте +, -, *, /, %");
                validOperation = false;
        }

        if (validOperation) {
            System.out.println("[SWITCH] Результат: " + num1 + " " + operator + " " + num2 + " = " + result);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите режим:");
        System.out.println("1 - Калькулятор с IF/ELSE");
        System.out.println("2 - Калькулятор с SWITCH");
        System.out.print("Ваш выбор: ");
        int choice = scanner.nextInt();

        System.out.print("Введите первое число: ");
        double num1 = scanner.nextDouble();

        System.out.print("Введите оператор (+, -, *, /, %): ");
        String operator = scanner.next();

        System.out.print("Введите второе число: ");
        double num2 = scanner.nextDouble();

        if (choice == 1) {
            calculatorIfElse(num1, operator, num2);
        } else if (choice == 2) {
            calculatorSwitch(num1, operator, num2);
        } else {
            System.out.println("Ошибка: неверный выбор режима!");
        }

        scanner.close();
    }
}