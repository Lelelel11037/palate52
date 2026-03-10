void main() {
  List<String> students = [
    'В Д В',
    'О А Э',
    'С С Р',
    'Ф Р Г',
    'Р К Н',
    'К Н Р'
  ];


  List<String> subjects = [
    'математика',
    'философия',
    'программирование',
    'испанский язык',
    'латинский язык'
  ];


  List<List<int>> grades = [
    [2, 4, 5, 4, 3],
    [4, 3, 4, 5, 4],
    [2, 3, 4, 5, 3],
    [5, 4, 4, 3, 5],
    [5, 5, 5, 5, 5],
    [4, 5, 3, 4, 4]
  ];


  print('Список студентов:');
  for (int i = 0; i < students.length; i++) {
    print('${i + 1}. ${students[i]}');
  }


  print('\n\nСписок предметов:');
  for (int i = 0; i < subjects.length; i++) {
    print('${i + 1}. ${subjects[i]}');
  }


  print('\n\nОценки студентов:');
  for (int i = 0; i < students.length; i++) {
    print('${students[i]}:');
    for (int j = 0; j < subjects.length; j++) {
      print('  ${subjects[j]}: ${grades[i][j]}');
    }
  }


  print('\n\nСредний балл по каждому предмету:');
  for (int j = 0; j < subjects.length; j++) {
    double sum = 0;
    for (int i = 0; i < students.length; i++) {
      sum += grades[i][j];
    }
    double avg = sum / students.length;
    print('${subjects[j]}: ${avg.toStringAsFixed(2)}');
  }


  print('\n\nСредний балл каждого студента:');
  List<double> studentAvg = [];
  for (int i = 0; i < students.length; i++) {
    double sum = 0;
    for (int j = 0; j < subjects.length; j++) {
      sum += grades[i][j];
    }
    double avg = sum / subjects.length;
    studentAvg.add(avg);
    print('${students[i]}: ${avg.toStringAsFixed(2)}');
  }
  int bestIndex = 0;
  for (int i = 1; i < studentAvg.length; i++) {
    if (studentAvg[i] > studentAvg[bestIndex]) {
      bestIndex = i;
    }
  }


  print('\n\nЛучший студент по среднему баллу: ${students[bestIndex]} (средний балл: ${studentAvg[bestIndex].toStringAsFixed(2)})');
  List<double> subjectAvg = [];
  for (int j = 0; j < subjects.length; j++) {
    double sum = 0;
    for (int i = 0; i < students.length; i++) {
      sum += grades[i][j];
    }
    subjectAvg.add(sum / students.length);
  }
  int worstSubjectIndex = 0;
  for (int j = 1; j < subjectAvg.length; j++) {
    if (subjectAvg[j] < subjectAvg[worstSubjectIndex]) {
      worstSubjectIndex = j;
    }
  }


  print('\n\nПредмет с наименьшим средним баллом: ${subjects[worstSubjectIndex]} (${subjectAvg[worstSubjectIndex].toStringAsFixed(2)})');
  double totalSum = 0;
  for (int i = 0; i < students.length; i++) {
    for (int j = 0; j < subjects.length; j++) {
      totalSum += grades[i][j];
    }
  }
  double totalAvg = totalSum / (students.length * subjects.length);


  print('\n\nОбщий средний балл по группе: ${totalAvg.toStringAsFixed(2)}');


  print('\n\nПеречень всех предметов (${subjects.length}):');
  for (int i = 0; i < subjects.length; i++) {
    print('  ${i + 1}. ${subjects[i]}');
  }


  print('\n\nСтуденты, у которых нет ни одной оценки 2:');
  for (int i = 0; i < students.length; i++) {
    bool hasTwo = false;
    for (int j = 0; j < subjects.length; j++) {
      if (grades[i][j] == 2) {
        hasTwo = true;
        break;
      }
    }
    if (!hasTwo) {
      print('  ${students[i]}');
    }
  }


  print('\n\nСтуденты, у которых все оценки не ниже 4:');
  for (int i = 0; i < students.length; i++) {
    bool allGood = true;
    for (int j = 0; j < subjects.length; j++) {
      if (grades[i][j] < 4) {
        allGood = false;
        break;
      }
    }
    if (allGood) {
      print('  ${students[i]}');
    }
  }
}