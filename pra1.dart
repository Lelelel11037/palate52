import 'dart:io';
import 'dart:math';
import 'package:pra1/pra1.dart' as pra1;

void main(List<String> arguments) {
  print("Число 1: ");
  var a = double.parse(stdin.readLineSync()!);
  print("Число 2: ");
  var b = double.parse(stdin.readLineSync()!);
  print("Оператор: ");
  String op = stdin.readLineSync()!;
  
  switch(op) {
    case '+':
      print(a+b);
      break;
    case '-':
      print(a-b);
      break;
    case '*':
      print(a*b);
      break;
    case '/':
      if(b == 0) {
        print('На ноль делить нельзя');
      } else {
        print(a/b);
      }
      break;
    case '~/':
      if(b == 0) {
        print('На ноль делить нельзя');
      } else {
        print(a~/b);
      }
      break;
    case '%':
      if(b == 0) {
        print('На ноль делить нельзя');
      } else {
        print(a%b);
      }
      break;
    case 'pow':
      print(pow(a, b));
      break;
    case '==':
      print(a == b);
      break;
    case '!=':
      print(a != b);
      break;
    case '>':
      print(a>b);
      break;
    case '<':
      print(a<b);
      break;
    case '>=':
      print(a>=b);
      break;
    case '<=':
      print(a<=b);
      break;
    case '&&':
      bool b = a != 0;
      bool b1 = b != 0;
      print("Результат: ${b && b1}");
      break;
    case '||':
      bool b = a != 0;
      bool b1 = b != 0;
      print(b || b1);
      break;
    case '!':
      bool b = a != 0;
      print(!b);
      break;
      
    default:
      print("Не то");
  }
}