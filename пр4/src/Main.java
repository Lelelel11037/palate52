import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();

        System.out.println("Выберите тип массива: ");
        System.out.println("1 - одномерный: ");
        System.out.println("2 - двумерный: ");
        int type = scanner.nextInt();

        if (type == 1) {
            System.out.print("Введите количество элементов (минимум 5): ");
            int n = scanner.nextInt();

            if (n < 5) {
                System.out.println("Нужно минимум 5 элементов.");
                return;
            }

            int[] array = new int[n];

            System.out.println("Заполните массив:");
            for (int i = 0; i < n; i++) {
                System.out.print("Элемент " + (i + 1) + ": ");
                array[i] = scanner.nextInt();
            }

            System.out.print("До сортировки: ");
            printArray(array);

            for (int i = 1; i < n; i++) {
                int key = array[i];
                int j = i - 1;
                while (j >= 0 && array[j] > key) {
                    array[j + 1] = array[j];
                    j--;
                }
                array[j + 1] = key;
            }

            System.out.print("После сортировки (по возрастанию): ");
            printArray(array);

            System.out.println("\nВыберите действие:");
            System.out.println("1 - поиск элемента по индексу");
            System.out.println("2 - изменить порядок элементов (от минимального к максимальному и наоборот)");
            int action = scanner.nextInt();

            if (action == 1) {
                System.out.print("Введите индекс для поиска (0-" + (n-1) + "): ");
                int index = scanner.nextInt();
                if (index >= 0 && index < n) {
                    System.out.println("Элемент по индексу " + index + ": " + array[index]);
                } else {
                    System.out.println("Неверный индекс.");
                }
            } else if (action == 2) {
                System.out.print("До разворота: ");
                printArray(array);

                for (int i = 0; i < n / 2; i++) {
                    int temp = array[i];
                    array[i] = array[n - 1 - i];
                    array[n - 1 - i] = temp;
                }

                System.out.print("После разворота (от максимального к минимальному): ");
                printArray(array);
            } else {
                System.out.println("Неверный выбор.");
            }

        } else if (type == 2) {
            System.out.print("Введите количество строк: ");
            int rows = scanner.nextInt();
            System.out.print("Введите количество столбцов: ");
            int cols = scanner.nextInt();

            int[][] matrix = new int[rows][cols];

            System.out.println("Сгенерированный двумерный массив:");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] = rand.nextInt(100);
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }

            int maxSum = Integer.MIN_VALUE;
            int maxRow = 0;

            for (int i = 0; i < rows; i++) {
                int sum = 0;
                for (int j = 0; j < cols; j++) {
                    sum += matrix[i][j];
                }
                System.out.println("Сумма строки " + i + ": " + sum);
                if (sum > maxSum) {
                    maxSum = sum;
                    maxRow = i;
                }
            }

            System.out.println("\nСтрока с максимальной суммой: " + maxRow);
            System.out.println("Сумма элементов в строке: " + maxSum);

        } else {
            System.out.println("Неверный выбор типа массива.");
        }

        scanner.close();
    }

    public static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}