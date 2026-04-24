// 1
class Mug {
  String beverage;
  Mug(this.beverage);
}

class Human {
  void drink(Mug mug) {
    print('Человек пьет ${mug.beverage}');
  }
}

void task1() {
  print(1);
  Human human = Human();
  Mug mug = Mug('чай');
  human.drink(mug);
}

// 2
class Wardrobe {
  List<String> items = [];
  
  void put(String item) {
    items.add(item);
    print('Положили $item в шкаф');
  }
  
  String take(String item) {
    items.remove(item);
    print('Забрали $item из шкафа');
    return item;
  }
}

void task2() {
  print('\n\n2');
  Wardrobe wardrobe = Wardrobe();
  wardrobe.put('куртка');
  wardrobe.put('шапка');
  wardrobe.take('куртка');
}

// 3
class Plate {
  double weight;
  Plate(this.weight);
}

class Barbell {
  double maxLoad;
  List<Plate> leftPlates = [];
  List<Plate> rightPlates = [];
  
  Barbell(this.maxLoad);
  
  void addLeft(Plate plate) {
    leftPlates.add(plate);
    print('Блин ${plate.weight}кг добавлен слева');
  }
  
  void addRight(Plate plate) {
    rightPlates.add(plate);
    print('Блин ${plate.weight}кг добавлен справа');
  }
}

void task3() {
  print('\n\n3');
  Barbell barbell = Barbell(200);
  barbell.addLeft(Plate(20));
  barbell.addRight(Plate(20));
}

// 4
class CurrencyConverter {
  double rubToUsd(double rub) {
    return rub / 90;
  }
  
  double usdToRub(double usd) {
    return usd * 90;
  }
  
  double rubToEur(double rub) {
    return rub / 100;
  }
  
  double eurToRub(double eur) {
    return eur * 100;
  }
}

void task4() {
  print('\n\n4');
  CurrencyConverter converter = CurrencyConverter();
  print('1000 RUB = ${converter.rubToUsd(1000)} USD');
  print('100 USD = ${converter.usdToRub(100)} RUB');
}

// 5
class Garage<T> {
  List<T> items = [];
  
  void park(T item) {
    items.add(item);
    print('Объект помещен в гараж');
  }
  
  T remove(int index) {
    return items.removeAt(index);
  }
}

void task5() {
  print('\n\n5');
  Garage<String> carGarage = Garage<String>();
  carGarage.park('Toyota');
  
  Garage<int> bikeGarage = Garage<int>();
  bikeGarage.park(123);
}

// 6
class Number {
  double value;
  Number(this.value);
  
  Number operator +(Number other) => Number(value + other.value);
  Number operator -(Number other) => Number(value - other.value);
  Number operator *(Number other) => Number(value * other.value);
  Number operator /(Number other) => Number(value / other.value);
  
  @override
  String toString() => value.toString();
}

void task6() {
  print('\n\n6');
  Number a = Number(10);
  Number b = Number(5);
  print('10 + 5 = ${a + b}');
  print('10 - 5 = ${a - b}');
}

// 7
enum CarState { stop, drive, turn }

class Car {
  CarState state = CarState.stop;
  
  void setState(CarState newState) {
    state = newState;
    print('Автомобиль: $state');
  }
}

void task7() {
  print('\n\n7');
  Car car = Car();
  car.setState(CarState.drive);
  car.setState(CarState.turn);
  car.setState(CarState.stop);
}

// 9
class NumberConverter {
  String decimalToHex(int decimal) {
    return decimal.toRadixString(16).toUpperCase();
  }
  
  String decimalToOctal(int decimal) {
    return decimal.toRadixString(8);
  }
  
  int hexToDecimal(String hex) {
    return int.parse(hex, radix: 16);
  }
  
  int octalToDecimal(String octal) {
    return int.parse(octal, radix: 8);
  }
}

void task9() {
  print('\n\n9');
  NumberConverter converter = NumberConverter();
  print('255 в HEX: ${converter.decimalToHex(255)}');
  print('255 в OCT: ${converter.decimalToOctal(255)}');
  print('FF в DEC: ${converter.hexToDecimal("FF")}');
}



void main() {
  task1();
  task2();
  task3();
  task4();
  task5();
  task6();
  task7();
  task9();
}
