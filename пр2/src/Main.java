import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        System.out.print("введите число, для таблицы умножения: ");
        int num = scanner.nextInt();

        System.out.print("введите 10, если хотите таблицу умножения от 1 до 10. введите 20, если хотите таблицу умножения от 1 до 20 ");
        int num2 = scanner.nextInt();

        if (num2 == 10){
            System.out.println("таблица уменожения от 1 до 10 для числа: "+ num);
            for(int i = 1; i <= num2; i++){
                int res = num * i;
                System.out.println(num + " * " + i + " = " +res);
            }
        }
        else if (num2 ==20){
            System.out.println("таблица уменожения от 1 до 20 для числа: "+ num);
            for(int i = 1; i<= num2; i++){
                int res = num * i;
                System.out.println(num + " * " + i + " = " +res);
            }
        }
        else if (num2!= 10 && num2!= 20){
            System.out.print("вы ввели неподходящее число");
        }
        else{
            if(num2<0){
                System.out.print("эта программа пока не может умножать на отрицательные числа");
            }
            else if(num2>20){
                System.out.print("эта программа пока не может умножать на числа больше 20");
            }
        }
        scanner.close();
    }
}