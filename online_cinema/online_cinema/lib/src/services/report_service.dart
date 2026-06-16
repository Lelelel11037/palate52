import 'dart:io';
import 'dart:async';
import '../data/repositories/movie_repository.dart';
import '../data/logger.dart';

class ReportService {
  final MovieRepository _movieRepo;
  final AppLogger _logger = AppLogger();

  ReportService(this._movieRepo);

  Future<Map<String, dynamic>> generateFullReport() async {
    _logger.log('Начало генерации отчета', level: LogLevel.info);
    
    final stopwatch = Stopwatch()..start();
    
    final report = await Future(() async {
      final movies = await _getMoviesAsync();
      
      final totalMovies = movies.length;
      final totalDuration = movies.fold<int>(0, (sum, movie) => sum + movie.duration);
      final avgDuration = totalMovies > 0 ? totalDuration / totalMovies : 0;
      
      final moviesByGenre = await _groupMoviesByGenreAsync(movies);
      
      final longestMovie = movies.isEmpty 
          ? null 
          : movies.reduce((a, b) => a.duration > b.duration ? a : b);
      
      stopwatch.stop();
      
      _logger.log('Отчет сгенерирован за ${_logger.formatDuration(stopwatch.elapsed)}', 
          level: LogLevel.success);
      
      return {
        'totalMovies': totalMovies,
        'totalDuration': totalDuration,
        'averageDuration': avgDuration,
        'moviesByGenre': moviesByGenre,
        'longestMovie': longestMovie,
        'generationTime': stopwatch.elapsedMilliseconds,
        'timestamp': DateTime.now().toIso8601String(),
      };
    });
    
    return report;
  }

  Future<List<dynamic>> _getMoviesAsync() async {
    await Future.delayed(Duration(milliseconds: 500));
    return _movieRepo.readAll();
  }

  Future<Map<int, List<dynamic>>> _groupMoviesByGenreAsync(List<dynamic> movies) async {
    final Map<int, List<dynamic>> grouped = {};
    
    for (var movie in movies) {
      await Future.delayed(Duration(milliseconds: 10));
      if (!grouped.containsKey(movie.genreId)) {
        grouped[movie.genreId] = [];
      }
      grouped[movie.genreId]!.add(movie);
    }
    
    return grouped;
  }

  Future<String> exportReportToFile() async {
    _logger.log('Экспорт отчета в файл', level: LogLevel.info);
    
    final report = await generateFullReport();
    final fileName = 'report_${DateTime.now().millisecondsSinceEpoch}.txt';
    final file = File(fileName);
    
    final content = StringBuffer();
    content.writeln('ОТЧЕТ ОНЛАЙН-КИНОТЕАТРА');
    content.writeln('Дата генерации: ${report['timestamp']}');
    content.writeln('Время генерации: ${report['generationTime']} мс');
    content.writeln('\nСтатистика:');
    content.writeln('- Всего фильмов: ${report['totalMovies']}');
    content.writeln('- Общая длительность: ${report['totalDuration']} мин');
    content.writeln('- Средняя длительность: ${report['averageDuration'].toStringAsFixed(1)} мин');
    
    if (report['longestMovie'] != null) {
      content.writeln('САМЫЙ ДЛИННЫЙ ФИЛЬМ:');
      content.writeln('- Название: ${report['longestMovie'].title}');
      content.writeln('- Длительность: ${report['longestMovie'].duration} мин');
      content.writeln();
    }
    
    content.writeln('РАСПРЕДЕЛЕНИЕ ПО ЖАНРАМ:');
    final genreNames = {
      1: 'Боевик', 2: 'Комедия', 3: 'Драма', 
      4: 'Фантастика', 5: 'Ужасы', 6: 'Романтика'
    };
    
    report['moviesByGenre'].forEach((genreId, movies) {
      final genreName = genreNames[genreId] ?? 'Жанр $genreId';
      content.writeln('- $genreName: ${movies.length} фильм(ов)');
    });
    
    content.writeln('=' * 50);
    
    await file.writeAsString(content.toString());
    _logger.log('Отчет экспортирован в файл: $fileName', level: LogLevel.success);
    
    return fileName;
  }
}