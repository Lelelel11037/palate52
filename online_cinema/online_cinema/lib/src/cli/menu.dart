import 'dart:io';
import 'input_helper.dart';
import '../data/repositories/movie_repository.dart';
import '../domain/models/movie.dart';
import '../data/logger.dart';
import '../services/report_service.dart';

void runMenu(MovieRepository movieRepo) {
  final logger = AppLogger();
  final reportService = ReportService(movieRepo);
  
  logger.log('Приложение запущено');
  
  while (true) {
    print('Онлайн кинотеатр');
    print('1. Добавить фильм');
    print('2. Показать все фильмы из БД');
    print('3. Удалить фильм');
    print('4. Показать статистику и отчет');
    print('5. Экспортировать отчет в файл');
    print('6. Показать логи');
    print('7. Очистить логи');
    print('0. Выход');
    stdout.write('Выберите действие: ');

    String? choice = stdin.readLineSync();
    switch (choice) {
      case '1':
        String title = InputHelper.askString('Введите название фильма');
        int duration = InputHelper.askInt('Введите длительность (мин)');
        int genreId = InputHelper.askInt('Введите ID жанра (1-Боевик, 2-Комедия, 3-Драма, 4-Фантастика, 5-Ужасы, 6-Романтика)');

        final movie = Movie(title: title, duration: duration, genreId: genreId);
        movieRepo.create(movie);
        print('Фильм успешно добавлен!');
        break;

      case '2':
        final movies = movieRepo.readAll();
        if (movies.isEmpty) {
          print('База данных пуста.');
        } else {
          print('\nСписок фильмов в БД');
          for (var m in movies) {
            print('ID: ${m.id} | Название: "${m.title}" | Длительность: ${m.duration} мин | ID Жанра: ${m.genreId}');
          }
        }
        break;

      case '3':
        int id = InputHelper.askInt('Введите ID фильма для удаления');
        if (movieRepo.delete(id)) {
          print('Фильм удален.');
        } else {
          print('Фильм с таким ID не найден.');
        }
        break;

      case '4':
        print('\nГенерация отчета');
        
        reportService.generateFullReport().then((report) {
          print('Статистика онлайн-кинотеатра');
          print('Всего фильмов: ${report['totalMovies']}');
          print('Общая длительность: ${report['totalDuration']} мин');
          print('Средняя длительность: ${report['averageDuration'].toStringAsFixed(1)} мин');
          print('Время генерации: ${report['generationTime']} мс');
          
          if (report['longestMovie'] != null) {
            print('\nСамый длинный фильм:');
            print('   Название: "${report['longestMovie'].title}"');
            print('   Длительность: ${report['longestMovie'].duration} мин');
          }
          
          print('\nРаспределение по жанрам:');
          final genreNames = {
            1: 'Боевик', 2: 'Комедия', 3: 'Драма', 
            4: 'Фантастика', 5: 'Ужасы', 6: 'Романтика'
          };
          report['moviesByGenre'].forEach((genreId, movies) {
            final genreName = genreNames[genreId] ?? 'Жанр $genreId';
            print('   ${genreName}: ${movies.length} фильм(ов)');
          });
        }).catchError((error) {
          print('Ошибка при генерации отчета: $error');
          logger.log('Ошибка генерации отчета: $error', level: LogLevel.error);
        });
        break;

      case '5':
        print('\nЭкспорт отчета в файл');
        print('Генерация и экспорт отчета');
        
        reportService.exportReportToFile().then((fileName) {
          print('Отчет успешно экспортирован в файл: $fileName');
          print('Файл сохранен в текущей директории');
        }).catchError((error) {
          print('Ошибка при экспорте отчета: $error');
          logger.log('Ошибка экспорта отчета: $error', level: LogLevel.error);
        });
        break;

      case '6':
        print('\nЛоги приложения');
        logger.getLogs().then((logs) {
          if (logs.isEmpty) {
            print('Логи отсутствуют');
          } else {
            final recentLogs = logs.length > 20 
                ? logs.sublist(logs.length - 20) 
                : logs;
            for (var log in recentLogs) {
              print(log);
            }
            if (logs.length > 20) {
              print('\n... и еще ${logs.length - 20} записей');
            }
          }
        });
        break;

      case '7':
        print('\nОчистка логов');
        logger.clearLogs().then((_) {
          print('Логи успешно очищены');
        });
        break;

      case '0':
        logger.log('Приложение закрыто пользователем');
        print('Выход из системы...');
        return;
        
      default:
        print('Неверный пункт меню.');
        logger.log('Выбрано неверное действие меню: $choice', level: LogLevel.warning);
    }
  }
}