// 1
String formatName(String firstName, String lastName, [String? patronymic]) {
  if (patronymic != null) {
    return '$lastName $firstName $patronymic';
  } else {
    return '$lastName $firstName';
  }
}


// 2
double? calculate(double a, double b, String operator) {
  switch (operator) {
    case '+':
      return a + b;
    case '-':
      return a - b;
    case '*':
      return a * b;
    case '/':
      if (b == 0) {
        return null;
      } else {
        return a / b;
      }
    default:
      return null;
  }
}


// 3
void countSigns(List<int> numbers) {
  int p = 0;
  int n = 0;
  int z = 0;
  for (int i in numbers) {
    if (i > 0) {
      p++;
    } else if (i < 0) {
      n++;
    } else {
      z++;
    }
  }

  print('+ : $p');
  print('- : $n');
  print('Нуль: $z');
}

// 4
List<int> transformList(List<int> numbers, int Function(int) transformer) {
  List<int> result = [];
  for (int i in numbers) {
    result.add(transformer(i));
  }

  return result;
}




void main() {
  // 1
 print(formatName("R", "G"));
 print(formatName("B", "O", "C"));
 print("\n");

  // 2
  print(calculate(10, 5, "+"));
  print("\n");

  // 3
  countSigns([-3, 1, 0, 3, 7, -13, 9]);
  print("\n");

  // 4
  List<int> myList = [1, 2, 3, 4, 5];
  print("\n");

  List<int> ku = transformList(myList, (x) => x * 2);
  print(ku);
  print("\n");





}