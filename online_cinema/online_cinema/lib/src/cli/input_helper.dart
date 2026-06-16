import 'dart:io';
import '../domain/validators/text_validator.dart';
import '../domain/validators/number_validator.dart';

class InputHelper {
  static String askString(String prompt) {
    while (true) {
      stdout.write('$prompt: ');
      String? input = stdin.readLineSync();
      if (validateRequiredText(input)) {
        return input!.trim();
      }
      print('Ошибка! Поле не должно быть пустым.');
    }
  }

  static int askInt(String prompt) {
    while (true) {
      stdout.write('$prompt: ');
      String? input = stdin.readLineSync();
      int? value = int.tryParse(input ?? '');
      if (validatePositiveNumber(value)) {
        return value!;
      }
      print('Ошибка! Введите число больше 0.');
    }
  }
}
