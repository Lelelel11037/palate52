import java.util.Scanner;

public class Main {

    public static int sloz(int[] nums) {
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            res = res + nums[i];
        }
        return res;
    }

    public static int vichret(int[] nums) {
        int res = nums[0];
        for (int i = 1; i < nums.length; i++) {
            res = res - nums[i];
        }
        return res;
    }

    public static int umnoz(int[] nums) {
        int res = 1;
        for (int i = 0; i < nums.length; i++) {
            res = res * nums[i];
        }
        return res;
    }

    public static float del(int[] nums) {
        float res = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == 0) {
                throw new ArithmeticException("делить на 0 нельзя");
            }
            res = res / nums[i];
        }
        return res;
    }

    public static int Del_Ost(int[] nums) {
        int res = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == 0) {
                throw new ArithmeticException("делить на 0 нельзя");
            }
            res = res % nums[i];
        }
        return res;
    }

    public static String ViborSistem(int res, int si) {
        if (si == 2) {
            return Integer.toBinaryString(res);
        }
        else if (si == 8) {
            return Integer.toOctalString(res);
        }
        else if (si == 10) {
            return Integer.toString(res);
        }
        else if (si == 16) {
            return Integer.toHexString(res).toUpperCase();
        }
        else {
            return Integer.toString(res);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("введите количество чисел (от 1 до 5): ");
        int count = scanner.nextInt();
        scanner.nextLine();

        if (count < 1 || count > 5) {
            System.out.println("число должно быть от 1 до 5");
            return;
        }

        int[] nums = new int[count];
        String[] actions = new String[count - 1];

        for (int i = 0; i < count; i++) {
            System.out.print("введите число " + (i + 1) + ": ");
            nums[i] = scanner.nextInt();
            scanner.nextLine();

            if (i < count - 1) {
                System.out.print("выберите действие для следующего числа (+, -, *, /, %): ");
                actions[i] = scanner.nextLine();
            }
        }

        System.out.print("выберите систему счисления для вывода результата (2, 8, 10, 16): ");
        int si = scanner.nextInt();

        try {
            float res = nums[0];
            boolean delenie = false;

            System.out.print("вычисления: " + nums[0]);

            for (int i = 1; i < count; i++) {
                System.out.print(" " + actions[i-1] + " " + nums[i]);

                switch (actions[i-1]) {
                    case "+":
                        res += nums[i];
                        break;
                    case "-":
                        res -= nums[i];
                        break;
                    case "*":
                        res *= nums[i];
                        break;
                    case "/":
                        if (nums[i] == 0) {
                            throw new ArithmeticException("делить на 0 нельзя");
                        }
                        res /= nums[i];
                        delenie = true;
                        break;
                    case "%":
                        if (nums[i] == 0) {
                            throw new ArithmeticException("делить на 0 нельзя");
                        }
                        res = res % nums[i];
                        break;
                    default:
                        System.out.println("неизвестное действие: " + actions[i-1]);
                        return;
                }
            }

            System.out.println(" = " + res);

            if (delenie && si != 10) {
                System.out.println("для деления с плавающей точкой доступна только десятичная система вывода.");
                System.out.println("результат: " + res);
            }
            else if (!delenie) {
                int IntRes = (int) res;
                String conv = ViborSistem(IntRes, si);
                System.out.println("результат в выбранной системе счисления: " + conv);
                System.out.println("результат в привычном формате: " + IntRes);
            }
            else {
                System.out.println("результат: " + res);
            }

        } catch (ArithmeticException e) {
            System.out.println("ошибка: " + e.getMessage());
        }
    }
}